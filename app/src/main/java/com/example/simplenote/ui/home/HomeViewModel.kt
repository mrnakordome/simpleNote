package com.example.simplenote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.remote.response.Note
import com.example.simplenote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val notes: List<Note> = emptyList(),
    val error: String? = null,
    val nextPageUrl: String? = null
) {
    val canPaginate: Boolean get() = nextPageUrl != null && !isLoadingMore
}

class HomeViewModel : ViewModel() {
    private val repository = NoteRepository(RetrofitInstance.api)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchNotes()
    }

    fun fetchNotes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = repository.getNotes()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notes = response.body()!!.results,
                            nextPageUrl = response.body()!!.next
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Failed to load notes.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun loadMoreNotes() {
        if (!_uiState.value.canPaginate) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            try {
                val nextUrl = _uiState.value.nextPageUrl!!
                val response = repository.getNotesByUrl(nextUrl)

                if (response.isSuccessful && response.body() != null) {
                    val newNotes = response.body()!!.results
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            notes = it.notes + newNotes,
                            nextPageUrl = response.body()!!.next
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoadingMore = false, error = "Failed to load more notes.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false, error = e.message) }
            }
        }
    }
}