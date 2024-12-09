package com.pakenanya.mindsync.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "documents")
data class DocumentsData(
    @PrimaryKey
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
    val filePath: String
)
