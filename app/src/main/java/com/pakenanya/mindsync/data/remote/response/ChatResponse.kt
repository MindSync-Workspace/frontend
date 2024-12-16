package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chats")
data class ChatsData(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("document_id")
    val documentId: Int,

    @field:SerializedName("org_id")
    val orgId: Int? = null,

    @field:SerializedName("is_human")
    val isHuman: Boolean,

    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("modified_at")
    val modifiedAt: String? = null,
)
