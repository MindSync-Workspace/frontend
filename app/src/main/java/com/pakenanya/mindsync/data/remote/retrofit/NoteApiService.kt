package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.NotesData
import retrofit2.Response
import retrofit2.http.*

data class CreateNoteRequest(
    val user_id: Int,
    val text: String
)

data class NoteSearchRequest(
    val n_items: Int,
    val text: String
)

interface NotesApiService {
    @POST("/api/notes")
    suspend fun createNote(
        @Body createNoteRequest: CreateNoteRequest
    ): Response<BaseResponse<NotesData>>

    @GET("/api/notes/{note_id}")
    suspend fun getNoteById(
        @Path("note_id") noteId: Int
    ): Response<BaseResponse<NotesData>>

    @PUT("/api/notes/{note_id}")
    suspend fun updateNote(
        @Path("note_id") noteId: Int,
        @Body createNoteRequest: CreateNoteRequest
    ): Response<BaseResponse<NotesData>>

    @DELETE("/api/notes/{note_id}")
    suspend fun deleteNote(
        @Path("note_id") noteId: Int
    ): Response<BaseResponse<Unit>>

    @GET("/api/notes/users/{user_id}")
    suspend fun getNotesByUserId(
        @Path("user_id") userId: Int
    ): Response<BaseResponse<List<NotesData>>>

    @GET("/api/notes/organizations/{organization_id}")
    suspend fun getNotesByOrgId(
        @Path("organization_id") orgId: Int
    ): Response<BaseResponse<List<NotesData>>>

    @GET("/api/notes/users/{user_id}/organizations/{organization_id}")
    suspend fun getNotesByUserAndOrg(
        @Path("user_id") userId: Int,
        @Path("organization_id") orgId: Int
    ): Response<BaseResponse<List<NotesData>>>

    @POST("/api/notes/users/{user_id}/search")
    suspend fun searchNotesByUser(
        @Path("user_id") userId: Int,
        @Body noteSearchRequest: NoteSearchRequest
    ): Response<BaseResponse<List<NotesData>>>
}