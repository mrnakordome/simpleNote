package com.example.simplenote.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.simplenote.App
import com.example.simplenote.data.remote.request.NoteRequest

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_OPERATION = "operation"
        const val KEY_NOTE_ID = "note_id"
        const val KEY_NOTE_TITLE = "note_title"
        const val KEY_NOTE_DESCRIPTION = "note_description"

        const val OP_CREATE = "create"
        const val OP_UPDATE = "update"
        const val OP_DELETE = "delete"
    }

    // Get instances of our repository and API
    private val noteRepository = (applicationContext as App).noteRepository
    private val apiService = com.example.simplenote.data.remote.RetrofitInstance.api

    override suspend fun doWork(): Result {
        val operation = inputData.getString(KEY_OPERATION) ?: return Result.failure()
        Log.d("SyncWorker", "Starting sync work for operation: $operation")

        return try {
            val success = when (operation) {
                OP_CREATE -> {
                    val tempId = inputData.getInt(KEY_NOTE_ID, 0)
                    val title = inputData.getString(KEY_NOTE_TITLE)!!
                    val description = inputData.getString(KEY_NOTE_DESCRIPTION)!!
                    val response = apiService.createNote(NoteRequest(title, description))
                    if (response.isSuccessful && response.body() != null) {
                        // After successful creation on server, update the local DB
                        // by deleting the temp note and inserting the real one.
                        noteRepository.syncCreatedNote(tempId, response.body()!!)
                        true
                    } else {
                        false
                    }
                }
                OP_UPDATE -> {
                    val id = inputData.getInt(KEY_NOTE_ID, 0)
                    val title = inputData.getString(KEY_NOTE_TITLE)!!
                    val description = inputData.getString(KEY_NOTE_DESCRIPTION)!!
                    val response = apiService.updateNote(id, NoteRequest(title, description))
                    response.isSuccessful
                }
                OP_DELETE -> {
                    val id = inputData.getInt(KEY_NOTE_ID, 0)
                    val response = apiService.deleteNote(id)
                    response.isSuccessful
                }
                else -> false
            }

            if (success) {
                Log.d("SyncWorker", "Sync work for operation: $operation SUCCESSFUL")
                Result.success()
            } else {
                Log.w("SyncWorker", "Sync work for operation: $operation FAILED. Will retry.")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Exception during sync work: ${e.message}. Will retry.")
            Result.retry()
        }
    }
}