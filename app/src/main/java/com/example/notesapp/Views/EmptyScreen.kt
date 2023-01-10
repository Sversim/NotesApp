package com.example.notesapp.Views

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.example.notesapp.MainViewModel
import com.example.notesapp.Navigation.NavRoute
import kotlinx.coroutines.launch

@Composable
fun EmptyScreen(navHostController: NavHostController, viewModel: MainViewModel) {
    Log.d("init", "init almost happened")


    val coroutineScope = rememberCoroutineScope()

    val getLocationOnClick: () -> Unit = {
        Log.d("init", "init key 1")
        Log.d("init", "init key 2")
        viewModel.initDatabase(){
            Log.d("init", "init happened")
            navHostController.navigate(route = NavRoute.StartScreen.route)
        }
    }

    getLocationOnClick()
}