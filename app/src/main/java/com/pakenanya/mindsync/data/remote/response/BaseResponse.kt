package com.pakenanya.mindsync.data.remote.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("meta")
    val meta: Meta,

    @SerializedName("data")
    val data: T
)

data class Meta(
    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)
