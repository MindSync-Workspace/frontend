package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.WhatsappData
import com.pakenanya.mindsync.data.remote.retrofit.WhatsappApiService
import org.json.JSONObject
import retrofit2.Response

class WhatsappRepository(
    private val whatsappApiService: WhatsappApiService
) {
    fun getSecretKeyByUserId(userId: Int): LiveData<Result<WhatsappData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(whatsappApiService.getSecretKeyByUserId(userId)) { responseBody ->
                responseBody.data
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