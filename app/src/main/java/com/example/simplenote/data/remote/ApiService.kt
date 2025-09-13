package com.example.simplenote.data.remote

import com.example.simplenote.data.remote.request.LoginRequest
import com.example.simplenote.data.remote.request.RegisterRequest
import com.example.simplenote.data.remote.response.LoginResponse
import com.example.simplenote.data.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

}