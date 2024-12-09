package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.local.room.MembershipsDao
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.MembershipData
import com.pakenanya.mindsync.data.remote.retrofit.MembershipsApiService
import org.json.JSONObject
import retrofit2.Response

class MembershipsRepository(
    private val membershipApiService: MembershipsApiService,
    private val membershipDao: MembershipsDao
) {
    fun createMembership(membership: Map<String, Any>): LiveData<Result<MembershipData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(membershipApiService.createMembership(membership)) { responseBody ->
                responseBody.data?.let { membershipDao.insertMembership(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getMembershipsByUserId(userId: Int): LiveData<Result<List<MembershipData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(membershipApiService.getMembershipsByUserId(userId)) { responseBody ->
                responseBody.data?.let { membershipDao.insertMemberships(it) }
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun updateMembership(
        membershipId: Int,
        membership: Map<String, Any>
    ): LiveData<Result<MembershipData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(
                membershipApiService.updateMembership(membershipId, membership)
            ) { responseBody ->
                responseBody.data?.let { membershipDao.updateMembership(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun deleteMembership(membershipId: Int): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(membershipApiService.deleteMembership(membershipId)) {
                membershipDao.deleteMembershipById(membershipId)
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
