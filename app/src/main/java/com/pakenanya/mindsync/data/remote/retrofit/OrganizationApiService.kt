package com.pakenanya.mindsync.data.remote.retrofit

import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.OrganizationData
import retrofit2.Response
import retrofit2.http.*

interface OrganizationsApiService {
    @GET("/api/organizations")
    suspend fun getAllOrganizations(): Response<BaseResponse<List<OrganizationData>>>

    @POST("/api/organizations")
    suspend fun createOrganization(
        @Body organization: Map<String, Any>
    ): Response<BaseResponse<OrganizationData>>

    @PUT("/api/organizations/{organization_id}")
    suspend fun updateOrganization(
        @Path("organization_id") organizationId: Int,
        @Body organization: Map<String, Any>
    ): Response<BaseResponse<OrganizationData>>

    @DELETE("/api/organizations/{organization_id}")
    suspend fun deleteOrganization(
        @Path("organization_id") organizationId: Int
    ): Response<BaseResponse<Unit>>
}