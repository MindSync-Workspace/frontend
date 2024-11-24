package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class UserResponse(
    @field:SerializedName("status_code")
    val statusCode: Int,

    @field:SerializedName("content")
    val content: UserContent,
)

data class UserContent(
    @field:SerializedName("meta")
    val meta: UserMeta,

    @field:SerializedName("data")
    val data: UserData,
)

data class UserMeta(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,
)

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("isActive")
    val isActive: Boolean? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("modifiedAt")
    val modifiedAt: String? = null
)