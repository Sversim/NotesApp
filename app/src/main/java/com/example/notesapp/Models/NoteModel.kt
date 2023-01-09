package com.example.notesapp.Models

import androidx.room.Entity

@Entity
data class NoteModel (
    val firebaseId: String,
    val title: String = "Test",
    val description: String = "",
    val time: String = "",
    val choosen: Boolean = false,
    val done: Boolean = false,
    val parent: String = ""
)