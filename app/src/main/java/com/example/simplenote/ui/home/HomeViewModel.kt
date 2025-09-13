package com.example.simplenote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenote.App
import com.example.simplenote.data.local.NoteEntity
import com.example.simplenote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val notes: List<NoteEntity> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val repository: NoteRepository = App.instance.noteRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            repository.allNotes.collect { notesFromDb ->
                _uiState.update { it.copy(notes = notesFromDb) }
            }
        }

        fetchNotes()
    }


    fun fetchNotes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.refreshNotes()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}