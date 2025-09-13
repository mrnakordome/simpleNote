package com.example.simplenote.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenote.App
import com.example.simplenote.data.AuthRepository
import com.example.simplenote.data.local.TokenManager
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.remote.response.UserInfoResponse
import com.example.simplenote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class SettingsUiState(
    val isLoading: Boolean = true,
    val userInfo: UserInfoResponse? = null,
    val error: String? = null,
    val passwordChangeSuccess: Boolean = false

)
class SettingsViewModel : ViewModel() {
    private val repository = AuthRepository(RetrofitInstance.api)
    private val tokenManager = TokenManager(App.instance)
    private val noteRepository: NoteRepository = App.instance.noteRepository

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(isLoading = true)
            try {
                val response = repository.getUserInfo()
                if (response.isSuccessful) {
                    _uiState.value = SettingsUiState(isLoading = false, userInfo = response.body())
                } else {
                    _uiState.value = SettingsUiState(isLoading = false, error = "Failed to fetch user info.")
                }
            } catch (e: Exception) {
                _uiState.value = SettingsUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun changePassword(oldPass: String, newPass: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, passwordChangeSuccess = false)
            try {
                val response = repository.changePassword(oldPass, newPass)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(isLoading = false, passwordChangeSuccess = true)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Incorrect old password or invalid new password.")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            noteRepository.clearLocalCache()
            tokenManager.clearTokens()
        }
    }

}