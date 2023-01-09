package com.example.notesapp.Database

import androidx.lifecycle.LiveData

class Repository {
    val readAll: LiveData<List<ListModel>>

    suspend fun create(list: ListModel, onSuccess: ()-> Unit)

    suspend fun update(list: ListModel, onSuccess: ()-> Unit)

    suspend fun delete(list: ListModel, onSuccess: ()-> Unit)

    fun signOut() {}

    fun connectToDatabase(onSuccess: () -> Unit, onFail: (String) -> Unit) {}
}

