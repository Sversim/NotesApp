package com.example.notesapp.Database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.notesapp.Models.ListModel
import com.example.notesapp.utils.AUTHOR_KEY
import com.example.notesapp.utils.AUTHOR_PAS
import com.google.firebase.auth.FirebaseAuth

class Repository {
    private val mAuth = FirebaseAuth.getInstance()
    val readAll: LiveData<List<ListModel>>
        get() = AllListsLiveData()

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

    fun connectToDatabase(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        mAuth.signInWithEmailAndPassword(AUTHOR_KEY, AUTHOR_PAS)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                mAuth.createUserWithEmailAndPassword(AUTHOR_KEY, AUTHOR_PAS)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFail(it.message.toString()) }
            }
    }
}

