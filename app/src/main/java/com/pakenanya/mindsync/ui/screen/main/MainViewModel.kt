package com.pakenanya.mindsync.ui.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.remote.retrofit.CreateNoteRequest
import com.pakenanya.mindsync.data.repository.DocumentsRepository
import com.pakenanya.mindsync.data.repository.NotesRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class DocumentState {
    data object Success : DocumentState()
    data object Loading : DocumentState()
}

sealed class NoteState {
    data object Success : NoteState()
    data object Loading : NoteState()
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

    private val _documentState = MutableLiveData<DocumentState>()
    val documentState : LiveData<DocumentState> = _documentState

    private val _noteState = MutableLiveData<NoteState>()
    val noteState : LiveData<NoteState> = _noteState

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
                        _documentState.value = DocumentState.Success
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
                        _noteState.value = NoteState.Success
                    }
                }
            }
        }
    }

    fun noteOnSubmitted(text: String) {
        _noteState.value = NoteState.Loading
        viewModelScope.launch {
            withTimeout(10_000) {
                userRepository.getUser().observeForever { result ->
                    if (result is Result.Success) {
                        val createNoteRequest = CreateNoteRequest(
                            user_id = result.data.id,
                            text = text
                        )
                        Log.e("Data Note", "$createNoteRequest")

                        notesRepository.createNote(createNoteRequest).observeForever { resultCreateNote ->
                            if (resultCreateNote is Result.Success) {
                                Log.e("result", "Berhasil membuat note")
                                getNotes()
                                _noteState.value = NoteState.Success
                            }
                        }
                    }
                }
            }
        }
    }

    fun documentOnSubmitted(file: MultipartBody.Part, title: String) {
        _documentState.value = DocumentState.Loading
        viewModelScope.launch {
            withTimeout(10_000) {
                userRepository.getUser().observeForever { result ->
                    if (result is Result.Success) {
                        val userId = result.data.id
                        documentsRepository.uploadDocument(file, userId, null, title, null).observeForever { resultUploadDoc ->
                            if (resultUploadDoc is Result.Success) {
                                Log.e("result2", "Berhasil upload document")
                                _documentState.value = DocumentState.Success
                                getDocuments()
                            }
                        }
                    }
                }
            }
        }
    }
}