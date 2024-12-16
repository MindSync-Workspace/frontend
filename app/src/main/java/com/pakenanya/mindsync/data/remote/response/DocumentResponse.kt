package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "documents")
data class DocumentsData(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("org_id")
    val orgId: Int? = null,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("file_path")
    val filePath: String,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("modified_at")
    val modifiedAt: String? = null,
)
