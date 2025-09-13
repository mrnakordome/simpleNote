package com.example.simplenote.data.repository

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.simplenote.data.local.NoteDao
import com.example.simplenote.data.local.NoteEntity
import com.example.simplenote.data.remote.ApiService
import com.example.simplenote.data.remote.request.NoteRequest
import com.example.simplenote.data.remote.response.Note
import com.example.simplenote.workers.SyncWorker
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class NoteRepository(
    private val apiService: ApiService,
    private val noteDao: NoteDao,
    private val context: Context
) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun refreshNotes() {
        try {
            val response = apiService.getNotes()
            if (response.isSuccessful && response.body() != null) {
                val notesFromServer = response.body()!!.results.map { networkNote ->
                    noteToNoteEntity(networkNote)
                }
                noteDao.insertAll(notesFromServer)
                Log.d("NoteRepository", "Notes refreshed from server and saved to DB.")
            }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Failed to refresh notes: ${e.message}")
        }
    }

    suspend fun createNote(title: String, description: String) {
        val tempId = -(System.currentTimeMillis().toInt())
        val newNoteEntity = NoteEntity(tempId, title, description, getCurrentTimestamp())
        noteDao.insert(newNoteEntity)

        try {
            val response = apiService.createNote(NoteRequest(title, description))
            if (response.isSuccessful && response.body() != null) {
                syncCreatedNote(tempId, response.body()!!)
            }
        } catch (e: Exception) {
            Log.w("NoteRepository", "Offline. Scheduling create note for later sync.")
            scheduleSync(SyncWorker.OP_CREATE, tempId, title, description)
        }
    }

    suspend fun updateNote(noteId: Int, title: String, description: String) {
        val updatedNoteEntity = NoteEntity(noteId, title, description, getCurrentTimestamp())
        noteDao.insert(updatedNoteEntity)

        try {
            apiService.updateNote(noteId, NoteRequest(title, description))
        } catch (e: Exception) {
            Log.w("NoteRepository", "Offline. Scheduling update note for later sync.")
            scheduleSync(SyncWorker.OP_UPDATE, noteId, title, description)
        }
    }

    suspend fun deleteNote(noteId: Int) {
        noteDao.deleteById(noteId)
        try {
            apiService.deleteNote(noteId)
        } catch (e: Exception) {
            Log.w("NoteRepository", "Offline. Scheduling delete note for later sync.")
            scheduleSync(SyncWorker.OP_DELETE, noteId)
        }
    }

    suspend fun syncCreatedNote(tempId: Int, realNote: Note) {
        noteDao.deleteById(tempId)
        noteDao.insert(noteToNoteEntity(realNote))
    }

    private fun scheduleSync(operation: String, noteId: Int, title: String? = null, description: String? = null) {
        val inputData = workDataOf(
            SyncWorker.KEY_OPERATION to operation,
            SyncWorker.KEY_NOTE_ID to noteId,
            SyncWorker.KEY_NOTE_TITLE to title,
            SyncWorker.KEY_NOTE_DESCRIPTION to description
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun noteToNoteEntity(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            title = note.title,
            description = note.description,
            updatedAt = note.updatedAt
        )
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
    }

    suspend fun clearLocalCache() {
        noteDao.clearAll()
    }


    fun getNoteById(noteId: Int): Flow<NoteEntity?> = noteDao.getNoteById(noteId)

}