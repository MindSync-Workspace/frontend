package com.pakenanya.mindsync.ui.screen.main.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.remote.retrofit.NoteSearchRequest
import com.pakenanya.mindsync.data.repository.NotesRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

    private val _notesData = MutableLiveData<List<NotesData>>()
    val notesData: LiveData<List<NotesData>> = _notesData

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query == "") {
            _notesData.value = emptyList()
        }
    }

    fun onSearchSubmitted() {
        val query = _searchQuery.value ?: ""
        if (query.isNotEmpty()) {
            Log.e("search", query)
            performSearch(query)
        } else {
            _notesData.value = emptyList()
            Log.e("search", "Query is empty, no search performed.")
        }
    }

    private fun performSearch(query: String) {
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                val searchParams = NoteSearchRequest(n_items = 3, text = query)
                notesRepository.searchNotesByUser(result.data.id, searchParams).observeForever { resultNotes ->
                    if (resultNotes is Result.Success) {
                        _notesData.value = resultNotes.data
                    }
                }
            }
        }
    }
}