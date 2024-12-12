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
import com.pakenanya.mindsync.ui.screen.main.document.model.ChatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        if (userId == 0) {
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
}