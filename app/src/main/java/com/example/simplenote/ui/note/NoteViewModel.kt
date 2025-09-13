package com.example.simplenote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simplenote.App
import com.example.simplenote.data.local.NoteEntity
import com.example.simplenote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NoteUiState(
    val isLoading: Boolean = true,
    val note: NoteEntity? = null,
    val error: String? = null,
    val actionSuccess: Boolean = false
)

class NoteViewModel(private val noteId: String?) : ViewModel() {
    private val repository: NoteRepository = App.instance.noteRepository

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            // Start listening to the specific note from the local database
            viewModelScope.launch {
                repository.getNoteById(noteId.toInt()).collect { noteEntity ->
                    _uiState.value = NoteUiState(isLoading = false, note = noteEntity)
                }
            }
        } else {
            // Ready for a new note, no loading needed.
            _uiState.value = NoteUiState(isLoading = false)
        }
    }

    fun saveNote(title: String, description: String) {
        viewModelScope.launch {
            if (noteId == null) {
                repository.createNote(title, description)
            } else {
                repository.updateNote(noteId.toInt(), title, description)
            }
            // In offline-first, the action is considered successful immediately
            // after being saved to the local DB. The sync happens in the background.
            _uiState.value = _uiState.value.copy(actionSuccess = true)
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            if (noteId != null) {
                repository.deleteNote(noteId.toInt())
                // Action is successful immediately.
                _uiState.value = _uiState.value.copy(actionSuccess = true)
            }
        }
    }
}

class NoteViewModelFactory(private val noteId: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}