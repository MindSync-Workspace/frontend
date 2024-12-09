package com.pakenanya.mindsync.ui.screen.main.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.repository.NotesRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val _noteData = MutableLiveData<NotesData>()
    val noteData: LiveData<NotesData> = _noteData

    fun getNote(id: Int) {
        notesRepository.getNoteById(id).observeForever { resultNotes ->
            if (resultNotes is Result.Success) {
                _noteData.value = resultNotes.data
            }
        }
    }
}