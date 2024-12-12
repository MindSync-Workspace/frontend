package com.pakenanya.mindsync.ui.screen.main.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.remote.retrofit.CreateNoteRequest
import com.pakenanya.mindsync.data.repository.NotesRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _noteData = MutableLiveData<NotesData>()
    val noteData: LiveData<NotesData> = _noteData

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> get() = _deleteResult

    fun getNote(id: Int) {
        notesRepository.getNoteById(id).observeForever { resultNotes ->
            if (resultNotes is Result.Success) {
                _noteData.value = resultNotes.data
            }
        }
    }

    fun updateNote(id: Int, newNotes: String) {
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                val createNoteRequest = CreateNoteRequest(
                    user_id = result.data.id,
                    text = newNotes
                )
                notesRepository.updateNoteById(id, createNoteRequest).observeForever { resultUpdate ->
                    if (resultUpdate is Result.Success) {
                        Log.e("Update Notes","Berhasil update note")
                        getNote(id)
                    }
                }
            }
        }
    }

    fun deleteNote(id: Int) {
        notesRepository.deleteNote(id).observeForever { result ->
            if (result is Result.Success) {
                Log.e("Delete Notes", "Berhasil hapus note")
                _deleteResult.value = Result.Success(true)
            } else {
                _deleteResult.value = Result.Success(false)
            }
        }
    }
}