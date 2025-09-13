package com.example.simplenote.data.remote

import com.example.simplenote.data.local.TokenManager
import com.example.simplenote.data.remote.request.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.*

class TokenAuthenticator(
    private val tokenManager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        return runBlocking {
            val tokenResponse = RetrofitInstance.api.refreshToken(RefreshTokenRequest(refreshToken))

            if (tokenResponse.isSuccessful && tokenResponse.body() != null) {
                val newAccessToken = tokenResponse.body()!!.accessToken
                tokenManager.saveAccessToken(newAccessToken)

                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                tokenManager.clearTokens()
                null
            }
        }
    }
}