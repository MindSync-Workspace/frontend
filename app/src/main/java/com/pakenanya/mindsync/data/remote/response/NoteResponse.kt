package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notes")
data class NotesData(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("org_id")
    val orgId: Int,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("modified_at")
    val modifiedAt: String? = null
)