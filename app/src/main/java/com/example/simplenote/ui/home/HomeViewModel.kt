package com.example.simplenote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.remote.response.Note
import com.example.simplenote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val notes: List<Note> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val repository = NoteRepository(RetrofitInstance.api)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchNotes()
    }

    fun fetchNotes() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            try {
                val response = repository.getNotes()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = HomeUiState(isLoading = false, notes = response.body()!!.results)
                } else {
                    _uiState.value = HomeUiState(isLoading = false, error = "Failed to load notes.")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState(isLoading = false, error = e.message)
            }
        }
    }
}