package com.example.simplenote.data

import com.example.simplenote.data.remote.ApiService
import com.example.simplenote.data.remote.request.ChangePasswordRequest
import com.example.simplenote.data.remote.request.LoginRequest
import com.example.simplenote.data.remote.request.RegisterRequest

class AuthRepository(private val apiService: ApiService) {
    suspend fun loginUser(request: LoginRequest) = apiService.login(request)
    suspend fun registerUser(request: RegisterRequest) = apiService.register(request)
    suspend fun getUserInfo() = apiService.getUserInfo()

    suspend fun changePassword(oldPass: String, newPass: String) =
        apiService.changePassword(ChangePasswordRequest(oldPass, newPass))
}