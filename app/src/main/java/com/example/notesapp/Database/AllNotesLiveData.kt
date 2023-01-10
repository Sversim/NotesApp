package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import com.example.notesapp.Models.ListModel
import com.example.notesapp.Models.NoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllNotesLiveData(listName: String): LiveData<List<NoteModel>>() {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference.child(mAuth.currentUser?.uid.toString()).child(listName)

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val notes = mutableListOf<NoteModel>()
            snapshot.children.map {
                notes.add(it.getValue(NoteModel::class.java) ?: NoteModel())
            }
            value = notes
        }

        override fun onCancelled(error: DatabaseError) {}

    }

    override fun onActive() {
        database.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        database.removeEventListener(listener)
        super.onInactive()
    }
}