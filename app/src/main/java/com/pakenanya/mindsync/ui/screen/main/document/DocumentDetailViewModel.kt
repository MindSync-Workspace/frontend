package com.pakenanya.mindsync.ui.screen.main.document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import com.pakenanya.mindsync.data.repository.DocumentsRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    private val documentsRepository: DocumentsRepository,
) : ViewModel() {

    private val _documentData = MutableLiveData<DocumentsData>()
    val documentData: LiveData<DocumentsData> = _documentData

    fun getDocument(documentId: Int) {
        documentsRepository.getDocumentById(documentId).observeForever { resultDocuments ->
            if (resultDocuments is Result.Success) {
                _documentData.value = resultDocuments.data
            }
        }
    }
}