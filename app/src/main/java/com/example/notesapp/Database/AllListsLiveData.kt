package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import com.example.notesapp.Models.ListModel
import com.example.notesapp.utils.AUTHOR_KEY
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllListsLiveData: LiveData<List<ListModel>>() {
    private val mAuth = AUTHOR_KEY
    private val  database = Firebase.database.reference.child(mAuth)

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val lists = mutableListOf<ListModel>()
            snapshot.children.map {
                lists.add(it.getValue(ListModel::class.java) ?: ListModel())
            }
            value = lists
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