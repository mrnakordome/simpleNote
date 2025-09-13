package com.example.simplenote

import android.app.Application
import com.example.simplenote.data.local.AppDatabase
import com.example.simplenote.data.remote.RetrofitInstance
import com.example.simplenote.data.repository.NoteRepository

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val noteRepository: NoteRepository by lazy {
        NoteRepository(
            apiService = RetrofitInstance.api,
            noteDao = database.noteDao(),
            context = applicationContext
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}