package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.MembershipData
import retrofit2.Response
import retrofit2.http.*

interface MembershipsApiService {
    @POST("/api/memberships")
    suspend fun createMembership(
        @Body membership: Map<String, Any>
    ): Response<BaseResponse<MembershipData>>

    @GET("/api/memberships/users/{user_id}")
    suspend fun getMembershipsByUserId(
        @Path("user_id") userId: Int
    ): Response<BaseResponse<List<MembershipData>>>

    @PUT("/api/memberships/{membership_id}")
    suspend fun updateMembership(
        @Path("membership_id") membershipId: Int,
        @Body membership: Map<String, Any>
    ): Response<BaseResponse<MembershipData>>

    @DELETE("/api/memberships/{membership_id}")
    suspend fun deleteMembership(
        @Path("membership_id") membershipId: Int
    ): Response<BaseResponse<Unit>>
}