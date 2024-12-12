package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.WhatsappData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WhatsappApiService {
    @GET("api/whatsapps/secret-key/{user_id}")
    suspend fun getSecretKeyByUserId(
        @Path("user_id") userId: Int
    ): Response<BaseResponse<WhatsappData>>
}