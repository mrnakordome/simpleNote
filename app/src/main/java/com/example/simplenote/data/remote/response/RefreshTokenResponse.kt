package com.example.simplenote.data.remote.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("access") val accessToken: String
)