package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.local.room.DocumentsDao
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import com.pakenanya.mindsync.data.remote.retrofit.DocumentsApiService
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response

class DocumentsRepository(
    private val documentsApiService: DocumentsApiService,
    private val documentsDao: DocumentsDao
) {
    fun uploadDocument(
        file: MultipartBody.Part,
        userId: Int,
        orgId: Int?,
        title: String,
        summary: String?
    ): LiveData<Result<DocumentsData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(
                documentsApiService.uploadDocument(file, userId, orgId, title, summary)
            ) { responseBody ->
                responseBody.data.let { documentsDao.insertDocument(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getDocuments(userId: Int): LiveData<Result<List<DocumentsData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(documentsApiService.getDocuments(userId)) { responseBody ->
                responseBody.data.let { documentsDao.insertDocuments(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getDocumentById(documentId: Int): LiveData<Result<DocumentsData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(documentsApiService.getDocumentById(documentId)) { responseBody ->
                responseBody.data.let { documentsDao.getDocumentById(it.id) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun updateDocument(documentId: Int, document: Map<String, Any>): LiveData<Result<DocumentsData>> =
        liveData {
            emit(Result.Loading)
            try {
                val result = handleResponse(
                    documentsApiService.updateDocument(documentId, document)
                ) { responseBody ->
                    responseBody.data.let { documentsDao.insertDocument(it) }
                    responseBody.data
                }
                emit(result)
            } catch (e: Exception) {
                emit(Result.Error("An error occurred: ${e.message}"))
            }
        }

    fun deleteDocument(documentId: Int): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(documentsApiService.deleteDocument(documentId)) { _ ->
                documentsDao.deleteDocumentById(documentId)
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun downloadDocument(documentId: Int): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(documentsApiService.downloadDocument(documentId)) { responseBody ->
                responseBody.data ?: ""
            }
            emit(result)
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
