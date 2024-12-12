package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.local.room.NotesDao
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.remote.retrofit.CreateNoteRequest
import com.pakenanya.mindsync.data.remote.retrofit.NoteSearchRequest
import com.pakenanya.mindsync.data.remote.retrofit.NotesApiService
import org.json.JSONObject
import retrofit2.Response

class NotesRepository(
    private val notesApiService: NotesApiService,
    private val notesDao: NotesDao
) {
    fun createNote(createNoteRequest: CreateNoteRequest): LiveData<Result<NotesData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(notesApiService.createNote(createNoteRequest)) { responseBody ->
                responseBody.data.let { notesDao.insertNote(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getNoteById(noteId: Int): LiveData<Result<NotesData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(notesApiService.getNoteById(noteId)) { responseBody ->
                responseBody.data.let { notesDao.insertNote(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getNotesByUserId(userId: Int): LiveData<Result<List<NotesData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(notesApiService.getNotesByUserId(userId)) { responseBody ->
                responseBody.data.let { notesDao.insertNotes(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getNotesByOrgId(orgId: Int): LiveData<Result<List<NotesData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(notesApiService.getNotesByOrgId(orgId)) { responseBody ->
                responseBody.data.let { notesDao.insertNotes(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getNotesByUserAndOrg(userId: Int, orgId: Int): LiveData<Result<List<NotesData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(
                notesApiService.getNotesByUserAndOrg(userId, orgId)
            ) { responseBody ->
                responseBody.data.let { notesDao.insertNotes(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun searchNotesByUser(userId: Int, searchParams: NoteSearchRequest): LiveData<Result<List<NotesData>>> =
        liveData {
            emit(Result.Loading)
            try {
                val result = handleResponse(
                    notesApiService.searchNotesByUser(userId, searchParams)
                ) { responseBody ->
                    responseBody.data ?: emptyList()
                }
                emit(result)
            } catch (e: Exception) {
                emit(Result.Error("An error occurred: ${e.message}"))
            }
        }

    fun updateNoteById(noteId: Int, createNoteRequest: CreateNoteRequest): LiveData<Result<NotesData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(notesApiService.updateNote(noteId, createNoteRequest)) { responseBody ->
                responseBody.data.let { notesDao.updateNote(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun deleteNote(noteId: Int): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(notesApiService.deleteNote(noteId)) { responseBody ->
                responseBody.data.let { notesDao.deleteNoteById(noteId) }
                responseBody.data
            }
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    private suspend fun <T> handleResponse(
        response: Response<BaseResponse<T>>,
        onSuccess: suspend (BaseResponse<T>) -> T
    ): Result<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                if (it.meta.status in 200..299) {
                    Result.Success(onSuccess(it))
                } else {
                    Result.Error(it.meta.message)
                }
            } ?: Result.Error("Empty response")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = if (errorBody != null) {
                JSONObject(errorBody).getJSONObject("meta").getString("message")
            } else {
                "Unknown error occurred"
            }
            Result.Error(errorMessage)
        }
    }
}