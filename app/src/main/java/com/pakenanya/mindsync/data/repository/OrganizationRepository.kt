package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.local.room.OrganizationsDao
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.OrganizationData
import com.pakenanya.mindsync.data.remote.retrofit.OrganizationsApiService
import org.json.JSONObject
import retrofit2.Response

class OrganizationsRepository(
    private val organizationsApiService: OrganizationsApiService,
    private val organizationsDao: OrganizationsDao
) {
    fun getAllOrganizations(): LiveData<Result<List<OrganizationData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(organizationsApiService.getAllOrganizations()) { responseBody ->
                responseBody.data?.let { organizationsDao.insertOrganizations(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun createOrganization(organization: Map<String, Any>): LiveData<Result<OrganizationData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(organizationsApiService.createOrganization(organization)) { responseBody ->
                responseBody.data?.let { organizationsDao.insertOrganization(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun updateOrganization(
        organizationId: Int,
        organization: Map<String, Any>
    ): LiveData<Result<OrganizationData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(
                organizationsApiService.updateOrganization(organizationId, organization)
            ) { responseBody ->
                responseBody.data?.let { organizationsDao.updateOrganization(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun deleteOrganization(organizationId: Int): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(organizationsApiService.deleteOrganization(organizationId)) {
                organizationsDao.deleteOrganizationById(organizationId)
                Unit
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