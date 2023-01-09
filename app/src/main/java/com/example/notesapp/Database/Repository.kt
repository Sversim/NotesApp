package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import com.example.notesapp.Models.ListModel

class Repository {
    val readAll: LiveData<List<ListModel>>
        get() = LiveData<List<ListModel>>()
        //TODO("Not yet implemented")

    suspend fun create(list: ListModel, onSuccess: ()-> Unit) {
        TODO("Not yet implemented")
    }

    suspend fun update(list: ListModel, onSuccess: ()-> Unit) {
        TODO("Not yet implemented")
    }

    suspend fun delete(list: ListModel, onSuccess: ()-> Unit) {
        TODO("Not yet implemented")
    }

    fun signOut() {}

    fun connectToDatabase(onSuccess: () -> Unit, onFail: (String) -> Unit) {}
}

