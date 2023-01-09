package com.example.notesapp.Models

import androidx.room.Entity
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class NoteModel (
    val firebaseId: String,
    val title: String = "Test",
    val description: String = "",
    val time: String = SimpleDateFormat("yyyy-MM-dd hh:mm").format(Calendar.getInstance().getTime()),
    val choosen: Boolean = false,
    val done: Boolean = false,
    val parent: String = ""
)