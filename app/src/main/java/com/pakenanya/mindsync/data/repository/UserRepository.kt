package com.pakenanya.mindsync.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pakenanya.mindsync.data.local.room.UserDao
import com.pakenanya.mindsync.data.remote.response.BaseResponse
import com.pakenanya.mindsync.data.remote.response.UserData
import com.pakenanya.mindsync.data.remote.retrofit.UserApiService
import com.pakenanya.mindsync.data.remote.retrofit.UserRegistrationRequest
import com.pakenanya.mindsync.data.remote.retrofit.UserUpdateRequest
import org.json.JSONObject
import retrofit2.Response

class UserRepository(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {
    fun createUser(userRegistrationRequest: UserRegistrationRequest, isRegister: Boolean): LiveData<Result<UserData>> = liveData {
        emit(Result.Loading)
        try {
            if (isRegister) {
                val result = handleResponse(userApiService.createUser(userRegistrationRequest)) { responseBody ->
                    responseBody.data
                }
                emit(result)
            } else {
                val user = UserData(
                    id = userRegistrationRequest.id!!,
                    username = userRegistrationRequest.username,
                    email = userRegistrationRequest.email,
                    password = userRegistrationRequest.password,
                    isActive = userRegistrationRequest.isActive
                )
                userDao.insertUser(user)
                emit(Result.Success(user))
            }
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getAllUsers(): LiveData<Result<List<UserData>>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(userApiService.getAllUsers()) { responseBody ->
                responseBody.data ?: emptyList()
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getUser(): LiveData<Result<UserData>> = liveData {
        emit(Result.Loading)
        try {
            val user = userDao.getUser()

            if (user != null) {
                emit(Result.Success(user))
            } else {
                emit(Result.Error("User not found"))
            }
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun getUserByEmail(email: String): LiveData<Result<UserData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(userApiService.getUserByEmail(email)) { responseBody ->
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun updateUser(
        userId: Int,
        userUpdateRequest: UserUpdateRequest
    ): LiveData<Result<UserData>> = liveData {
        emit(Result.Loading)
        try {
            val result = handleResponse(userApiService.updateUser(userId, userUpdateRequest)) { responseBody ->
                responseBody.data.let { userDao.insertUser(it) }
                responseBody.data
            }
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message}"))
        }
    }

    fun deleteUser(): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            userDao.clearAllUsers()
            emit(Result.Success(Unit))
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
