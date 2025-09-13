package com.example.simplenote.data.remote.response

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("creator_name") val creatorName: String,
    @SerializedName("creator_username") val creatorUsername: String
)