package com.pakenanya.mindsync.ui.screen.main.document

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.remote.response.ChatsData
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import com.pakenanya.mindsync.data.remote.retrofit.CreateChatRequest
import com.pakenanya.mindsync.data.repository.ChatsRepository
import com.pakenanya.mindsync.data.repository.DocumentsRepository
import com.pakenanya.mindsync.data.repository.Result
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    private val documentsRepository: DocumentsRepository,
    private val chatsRepository: ChatsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _documentData = MutableLiveData<DocumentsData>()
    val documentData: LiveData<DocumentsData> = _documentData

    private val _chatsData = MutableLiveData<List<ChatsData>>(emptyList())
    val chatsData: LiveData<List<ChatsData>> = _chatsData

    private var _userId: Int = 0
    var userId: Int = _userId

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> get() = _deleteResult

    fun getDocument(documentId: Int) {
        documentsRepository.getDocumentById(documentId).observeForever { resultDocuments ->
            if (resultDocuments is Result.Success) {
                _documentData.value = resultDocuments.data
                getChats(resultDocuments.data.id)
            }
        }
    }

    fun getChats(documentId: Int) {
        chatsRepository.getChatsByDocumentId(documentId).observeForever { result ->
            if (result is Result.Success) {
                _chatsData.value = result.data
            }
        }
    }

    fun sendChat(documentId: Int, message: String) {
        if (userId == 0 && message != "") {
            userRepository.getUser().observeForever { result ->
                if (result is Result.Success) {
                    _userId = result.data.id
                    val createChatRequest = CreateChatRequest(
                        document_id = documentId,
                        user_id = result.data.id,
                        is_human = true,
                        text = message
                    )
                    chatsRepository.createChat(createChatRequest).observeForever { resultCreate ->
                        if (resultCreate is Result.Success) {
                            Log.e("Send Chat", "Berhasil kirim pesan")
                            getChats(documentId)
                        }
                    }
                }
            }
        } else {
            val createChatRequest = CreateChatRequest(
                document_id = documentId,
                user_id = userId,
                is_human = true,
                text = message
            )
            chatsRepository.createChat(createChatRequest).observeForever { result ->
                if (result is Result.Success) {
                    Log.e("Send Chat", "Berhasil kirim pesan")
                    getChats(documentId)
                }
            }
        }
    }

    fun deleteDoc(id: Int) {
        documentsRepository.deleteDocument(id).observeForever { result ->
            if (result is Result.Success) {
                Log.e("Delete Document", "Berhasil hapus dokumen")
                _deleteResult.value = Result.Success(true)
            } else {
                _deleteResult.value = Result.Success(false)
            }
        }
    }
}