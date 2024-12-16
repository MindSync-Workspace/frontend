package com.pakenanya.mindsync.data.remote.response

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class WhatsappData(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("number")
    val number: String? = null,

    @field:SerializedName("secret_key")
    val secretKey: String,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("modified_at")
    val modifiedAt: String? = null
)