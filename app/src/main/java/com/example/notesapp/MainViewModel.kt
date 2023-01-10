package com.example.notesapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.Database.Repository
import com.example.notesapp.utils.AUTHOR
import com.example.notesapp.utils.AUTHOR_KEY
import com.example.notesapp.utils.REPOSITORY

class MainViewModel (application: Application) : AndroidViewModel(application) {
    val context = application

    val sPreferences = context.getSharedPreferences(AUTHOR, Context.MODE_PRIVATE)

    fun initDatabase(onSuccess: ()-> Unit) {
        if (sPreferences.getString(AUTHOR, "").isNullOrEmpty()) {
            val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            AUTHOR_KEY = (1..10)
                        .map { charset.random() }
                    .joinToString("")
            sPreferences.edit()
                .putString(AUTHOR, AUTHOR_KEY)
                .apply()
        } else {
            AUTHOR_KEY = sPreferences.getString(AUTHOR, "")!!;
        }

        REPOSITORY = Repository()
        REPOSITORY.connectToDatabase(
            { onSuccess() },
            { Log.d("checkData", "Error: ${it}") }
        )
    }

    fun readAllLists() = REPOSITORY.readAll
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application = application) as T
    }
}