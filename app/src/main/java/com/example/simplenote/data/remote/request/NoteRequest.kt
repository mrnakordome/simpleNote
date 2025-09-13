package com.example.simplenote.data.remote.request

import com.google.gson.annotations.SerializedName

data class NoteRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)