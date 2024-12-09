package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface DocumentsApiService {
    @Multipart
    @POST("/api/documents/upload")
    suspend fun uploadDocument(
        @Part file: MultipartBody.Part,
        @Part("user_id") userId: Int,
        @Part("org_id") orgId: Int?,
        @Part("title") title: String,
        @Part("summary") summary: String?
    ): Response<BaseResponse<DocumentsData>>

    @GET("/api/documents/{document_id}")
    suspend fun getDocumentById(
        @Path("document_id") documentId: Int
    ): Response<BaseResponse<DocumentsData>>

    @PUT("/api/documents/{document_id}")
    suspend fun updateDocument(
        @Path("document_id") documentId: Int,
        @Body document: Map<String, Any>
    ): Response<BaseResponse<DocumentsData>>

    @DELETE("/api/documents/{document_id}")
    suspend fun deleteDocument(
        @Path("document_id") documentId: Int
    ): Response<BaseResponse<Unit>>

    @GET("/api/documents")
    suspend fun getDocuments(
        @Query("user_id") userId: Int
    ): Response<BaseResponse<List<DocumentsData>>>

    @GET("/api/documents/download/{document_id}")
    suspend fun downloadDocument(
        @Path("document_id") documentId: Int
    ): Response<BaseResponse<String>>
}
