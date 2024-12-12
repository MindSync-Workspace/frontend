package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.ChatsData
import retrofit2.Response
import retrofit2.http.*

data class CreateChatRequest(
    val document_id: Int,
    val user_id: Int,
    val is_human: Boolean,
    val text: String
)
interface ChatsApiService {
    @POST("/api/chats")
    suspend fun createChat(
        @Body createChatRequest: CreateChatRequest
    ): Response<BaseResponse<ChatsData>>

    @GET("/api/chats/documents/{document_id}")
    suspend fun getChatsByDocumentId(
        @Path("document_id") documentId: Int
    ): Response<BaseResponse<List<ChatsData>>>

    @PUT("/api/chats/{chat_id}")
    suspend fun updateChat(
        @Path("chat_id") chatId: Int,
        @Body chat: Map<String, Any>
    ): Response<BaseResponse<ChatsData>>

    @DELETE("/api/chats/{chat_id}")
    suspend fun deleteChat(
        @Path("chat_id") chatId: Int
    ): Response<BaseResponse<Unit>>
}
