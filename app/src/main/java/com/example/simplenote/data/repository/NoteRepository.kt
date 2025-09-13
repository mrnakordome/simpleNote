package com.example.simplenote.data.repository

import com.example.simplenote.data.remote.ApiService
import com.example.simplenote.data.remote.request.NoteRequest

class NoteRepository(private val apiService: ApiService) {
    suspend fun getNotes() = apiService.getNotes()

    suspend fun getNotesByUrl(url: String) = apiService.getNotesByUrl(url)

    suspend fun createNote(title: String, description: String) = apiService.createNote(NoteRequest(title, description))
    suspend fun getNoteById(id: Int) = apiService.getNoteById(id)
    suspend fun updateNote(id: Int, title: String, description: String) = apiService.updateNote(id, NoteRequest(title, description))
    suspend fun deleteNote(id: Int) = apiService.deleteNote(id)
}