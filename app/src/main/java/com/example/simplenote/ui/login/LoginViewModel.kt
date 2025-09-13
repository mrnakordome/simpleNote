package com.example.simplenote.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenote.App
import com.example.simplenote.data.AuthRepository
import com.example.simplenote.data.local.TokenManager
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.remote.request.LoginRequest
import com.example.simplenote.data.remote.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository(RetrofitInstance.api)
    private val tokenManager = TokenManager(App.instance)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, pass: String) {
        _uiState.value = LoginUiState() // Reset state
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            try {
                val response = repository.loginUser(LoginRequest(username, pass))
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    tokenManager.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
                    _uiState.value = LoginUiState(loginSuccess = true)
                } else {
                    val errorMessage = parseError(response)
                    _uiState.value = LoginUiState(error = errorMessage)
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState(error = "Network error: ${e.message}")
            }
        }
    }

    private fun parseError(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            errorResponse.errors?.firstOrNull()?.detail ?: "An unknown error occurred"
        } catch (e: Exception) {
            "Error parsing response"
        }
    }
}