package com.example.simplenote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.remote.response.Note
import com.example.simplenote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NoteUiState(
    val isLoading: Boolean = true,
    val note: Note? = null,
    val error: String? = null,
    val actionSuccess: Boolean = false
)

class NoteViewModel(private val noteId: String?) : ViewModel() {

    private val repository = NoteRepository(RetrofitInstance.api)

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState

    init {
        if (noteId != null) {
            fetchNoteDetails()
        } else {
            _uiState.value = NoteUiState(isLoading = false)
        }
    }

    private fun fetchNoteDetails() {
        viewModelScope.launch {
            _uiState.value = NoteUiState(isLoading = true)
            try {
                val id = noteId!!.toInt()
                val response = repository.getNoteById(id)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = NoteUiState(isLoading = false, note = response.body())
                } else {
                    _uiState.value = NoteUiState(isLoading = false, error = "Failed to load note.")
                }
            } catch (e: Exception) {
                _uiState.value = NoteUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun saveNote(title: String, description: String) {
        viewModelScope.launch {
            try {
                val response = if (noteId == null) {
                    repository.createNote(title, description)
                } else {
                    repository.updateNote(noteId.toInt(), title, description)
                }

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(actionSuccess = true)
                } else {
                    _uiState.value = _uiState.value.copy(error = "Failed to save note.")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            if (noteId != null) {
                try {
                    val response = repository.deleteNote(noteId.toInt())
                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(actionSuccess = true)
                    } else {
                        _uiState.value = _uiState.value.copy(error = "Failed to delete note.")
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
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