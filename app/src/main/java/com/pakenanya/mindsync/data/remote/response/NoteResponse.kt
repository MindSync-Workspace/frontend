package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class NoteResponse(
    @field:SerializedName("status_code")
    val statusCode: Int,

    @field:SerializedName("content")
    val content: NoteContent,
)

data class NoteContent(
    @field:SerializedName("meta")
    val meta: NoteMeta,

    @field:SerializedName("data")
    val data: NoteData,
)

data class NoteMeta(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,
)

@Entity(tableName = "notes")
data class NoteData(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("isActive")
    val isActive: Boolean? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("modifiedAt")
    val modifiedAt: String? = null
)