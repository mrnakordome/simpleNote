package com.example.simplenote.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenote.data.AuthRepository
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.remote.request.RegisterRequest
import com.example.simplenote.data.remote.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

data class RegisterUiState(
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository(RetrofitInstance.api)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(first: String, last: String, user: String, email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            try {
                val request = RegisterRequest(
                    firstName = first,
                    lastName = last,
                    username = user,
                    email = email,
                    password = pass
                )
                val response = repository.registerUser(request)
                if (response.isSuccessful) {
                    _uiState.value = RegisterUiState(registerSuccess = true)
                } else {
                    val errorMessage = parseError(response)
                    _uiState.value = RegisterUiState(error = errorMessage)
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState(error = "Network error: ${e.message}")
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