package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DocumentResponse(
    @field:SerializedName("status_code")
    val statusCode: Int,

    @field:SerializedName("content")
    val content: UserContent,
)

data class DocumentContent(
    @field:SerializedName("meta")
    val meta: DocumentMeta,

    @field:SerializedName("data")
    val data: DocumentData,
)

data class DocumentMeta(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,
)

@Entity(tableName = "documents")
data class DocumentData(
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