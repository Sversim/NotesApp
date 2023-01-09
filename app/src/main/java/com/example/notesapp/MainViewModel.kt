package com.example.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.Database.Repository

class MainViewModel (application: Application) : AndroidViewModel(application) {

    val context = application

    fun initDatabase(type: String, onSuccess: ()-> Unit) {
//        when(type) {
//            TYPE_ROOM -> {
//                val dao = AppRoomDatabase.getInstance(context = context).getRoomDao()
//                REPOSITORY = RoomRepository(dao)
//                onSuccess()
//            }
//            TYPE_FIREBASE -> {
//                REPOSITORY = AppFirebaseRepository()
//                REPOSITORY.connectToDatabase(
//                    { onSuccess() },
//                    { Log.d("checkData", "Error: ${it}")}
//                )
//            }
//        }
    }

    fun readAllLists() = Repository.readAll
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application = application) as T
    }
}