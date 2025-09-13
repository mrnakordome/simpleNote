package com.example.simplenote.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access")
    val accessToken: String,

    @SerializedName("refresh")
    val refreshToken: String
)