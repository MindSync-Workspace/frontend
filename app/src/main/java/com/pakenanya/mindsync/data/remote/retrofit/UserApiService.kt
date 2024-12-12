package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class UserRegistrationRequest(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val isActive: Boolean
)

data class UserUpdateRequest(
    val username: String
)

interface UserApiService {
    @POST("api/users")
    suspend fun createUser(
        @Body userRegistrationRequest: UserRegistrationRequest
    ): Response<BaseResponse<UserData>>

    @GET("api/users")
    suspend fun getAllUsers(): Response<BaseResponse<List<UserData>>>

    @GET("api/users/{email}")
    suspend fun getUserByEmail(
        @Path("email") email: String
    ): Response<BaseResponse<UserData>>

    @PUT("api/users/{user_id}")
    suspend fun updateUser(
        @Path("user_id") userId: Int,
        @Body userUpdateRequest: UserUpdateRequest
    ): Response<BaseResponse<UserData>>

    @DELETE("api/users/{user_id}")
    suspend fun deleteUser(
        @Path("user_id") userId: Int
    ): Response<BaseResponse<Unit>>
}