package com.example.notesapp.Database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.notesapp.Models.ListModel
import com.example.notesapp.utils.AUTHOR_KEY
import com.example.notesapp.utils.AUTHOR_PAS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Repository {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference

    val readAllLists: LiveData<List<ListModel>>
        get() = AllListsLiveData()

    fun createList(listModel: ListModel, onSuccess: ()-> Unit) {
        val listId = database.push().key.toString()
        val mapList = hashMapOf<String, Any?>()

        mapList["firebaseId"] = listId
        mapList["name"] = listModel.name
        database.child(mAuth.currentUser?.uid.toString()).child(listId)
            .updateChildren(mapList)
            .addOnSuccessListener {onSuccess()}
            .addOnFailureListener { Log.d("create", "Creation failure") }
    }

    fun updateList(listModel: ListModel, onSuccess: ()-> Unit) {
        val mapList = hashMapOf<String, Any?>()

        mapList["firebaseId"] = listModel.firebaseId
        mapList["name"] = listModel.name
        database.child(mAuth.currentUser?.uid.toString()).child(listModel.firebaseId)
            .updateChildren(mapList)
            .addOnSuccessListener {onSuccess()}
            .addOnFailureListener { Log.d("create", "Creation failure") }
    }

    fun deleteList(list: ListModel, onSuccess: ()-> Unit) {
        database.child(list.firebaseId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { Log.d("checkData", "Failed to delete note") }
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

