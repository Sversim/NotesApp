package com.example.notesapp.Database

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.notesapp.Models.ListModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


const val AOUTHOR = "AOUTHOR"

class AllListsLiveData: LiveData<List<ListModel>>() {
//    TODO SharedPreferences
//    var myPrefs: SharedPreferences? = activity!!.getSharedPreferences("myPrefs", MODE_PRIVATE)
//    private val mAuth = FirebaseAuth.getInstance()
    private val  database = Firebase.database.reference
        .child(mAuth.currentUser?.uid.toString())

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val notes = mutableListOf<ListModel>()
            snapshot.children.map {
                notes.add(it.getValue(ListModel::class.java) ?: ListModel())
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