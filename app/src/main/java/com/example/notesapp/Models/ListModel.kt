package com.example.notesapp.Models

import androidx.room.Entity

@Entity
data class ListModel (
    val firebaseId: String = "",
    val name: String = "New"
)