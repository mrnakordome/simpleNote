package com.example.simplenote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val updatedAt: String
)