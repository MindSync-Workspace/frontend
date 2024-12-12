package com.pakenanya.mindsync.ui.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.remote.retrofit.CreateNoteRequest
import com.pakenanya.mindsync.data.repository.DocumentsRepository
import com.pakenanya.mindsync.data.repository.NotesRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class ContentState {
    data object Success : ContentState()
    data object Loading : ContentState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val documentsRepository: DocumentsRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _documentsData = MutableLiveData<List<DocumentsData>>()
    val documentsData: LiveData<List<DocumentsData>> = _documentsData

    private val _notesData = MutableLiveData<List<NotesData>>()
    val notesData: LiveData<List<NotesData>> = _notesData

    private val _contentState = MutableLiveData<ContentState>()
    val contentState : LiveData<ContentState> = _contentState

    init {
        getDocuments()
        getNotes()
    }

    fun getDocuments() {
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                documentsRepository.getDocuments(result.data.id).observeForever { resultDocuments ->
                    if (resultDocuments is Result.Success) {
                        _documentsData.value = resultDocuments.data
                    }
                }
            }
        }
    }

    fun getNotes() {
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                notesRepository.getNotesByUserId(result.data.id).observeForever { resultNotes ->
                    if (resultNotes is Result.Success) {
                        _notesData.value = resultNotes.data
                    }
                }
            }
        }
    }

    fun noteOnSubmitted(text: String) {
        _contentState.value = ContentState.Loading
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                val createNoteRequest = CreateNoteRequest(
                    user_id = result.data.id,
                    text = text
                )

                notesRepository.createNote(createNoteRequest).observeForever { resultCreateNote ->
                    if (resultCreateNote is Result.Success) {
                        Log.e("result", "Berhasil membuat note")
                        getNotes()
                        _contentState.value = ContentState.Success
                    }
                }
            }
        }
    }

    fun documentOnSubmitted(file: MultipartBody.Part, title: String) {
        _contentState.value = ContentState.Loading
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                val userId = result.data.id
                documentsRepository.uploadDocument(file, userId, null, title, null).observeForever { resultUploadDoc ->
                    if (resultUploadDoc is Result.Success) {
                        Log.e("result", "Berhasil upload document")
                        getDocuments()
                        _contentState.value = ContentState.Success
                    }
                }
            }
        }
    }
}