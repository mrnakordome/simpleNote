package com.example.simplenote.data.remote

import com.example.simplenote.data.remote.request.ChangePasswordRequest
import com.example.simplenote.data.remote.request.LoginRequest
import com.example.simplenote.data.remote.request.NoteRequest
import com.example.simplenote.data.remote.request.RefreshTokenRequest
import com.example.simplenote.data.remote.request.RegisterRequest
import com.example.simplenote.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/notes/")
    suspend fun getNotes(): Response<NoteListResponse>

    @GET
    suspend fun getNotesByUrl(@Url url: String): Response<NoteListResponse>

    @POST("api/notes/")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<Note>

    @GET("api/notes/{id}/")
    suspend fun getNoteById(@Path("id") id: Int): Response<Note>

    @PATCH("api/notes/{id}/")
    suspend fun updateNote(@Path("id") id: Int, @Body noteRequest: NoteRequest): Response<Note>

    @DELETE("api/notes/{id}/")
    suspend fun deleteNote(@Path("id") id: Int): Response<Unit>

    @GET("api/auth/userinfo/")
    suspend fun getUserInfo(): Response<UserInfoResponse>

    @POST("api/auth/change-password/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Unit>

    @POST("api/auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

}