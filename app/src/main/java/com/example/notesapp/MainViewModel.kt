package com.example.notesapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.Database.Repository
import com.example.notesapp.utils.*

class MainViewModel (application: Application) : AndroidViewModel(application) {
    val context = application

    val sPreferences = context.getSharedPreferences(AUTHOR, Context.MODE_PRIVATE)

    fun initDatabase(onSuccess: ()-> Unit) {
        if (sPreferences.getString(AUTHOR, "").isNullOrEmpty()) {
            val charset = ('a'..'z') + ('A'..'Z')
            AUTHOR_KEY = (1..10)
                        .map { charset.random() }
                    .joinToString("") + "@mail.ru"
            sPreferences.edit()
                .putString(AUTHOR, AUTHOR_KEY)
                .apply()
        } else {
            AUTHOR_KEY = sPreferences.getString(AUTHOR, "")!!;
        }

        if (sPreferences.getString(PASSWORD, "").isNullOrEmpty()) {
            val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            AUTHOR_PAS = (1..6)
                .map { charset.random() }
                .joinToString("")
            sPreferences.edit()
                .putString(PASSWORD, AUTHOR_PAS)
                .apply()
        } else {
            AUTHOR_PAS = sPreferences.getString(PASSWORD, "")!!;
        }

        Log.d("log", AUTHOR_KEY)
        Log.d("pas", AUTHOR_PAS)

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