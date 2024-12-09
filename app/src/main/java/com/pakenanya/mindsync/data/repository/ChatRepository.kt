package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.local.room.ChatsDao
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.ChatsData
import com.pakenanya.mindsync.data.remote.retrofit.ChatsApiService
import org.json.JSONObject
import retrofit2.Response

class ChatsRepository(
    private val chatsApiService: ChatsApiService,
    private val chatsDao: ChatsDao
) {
    fun createChat(chat: Map<String, Any>): LiveData<Result<ChatsData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(chatsApiService.createChat(chat)) { responseBody ->
                responseBody.data?.let { chatsDao.insertChat(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getChatsByDocumentId(documentId: Int): LiveData<Result<List<ChatsData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(chatsApiService.getChatsByDocumentId(documentId)) { responseBody ->
                responseBody.data?.let { chatsDao.insertChats(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun updateChat(chatId: Int, chat: Map<String, Any>): LiveData<Result<ChatsData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(chatsApiService.updateChat(chatId, chat)) { responseBody ->
                responseBody.data?.let { chatsDao.insertChat(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun deleteChat(chatId: Int): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(chatsApiService.deleteChat(chatId)) { _ ->
                chatsDao.deleteChatById(chatId)
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
