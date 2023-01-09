package com.example.notesapp.Views

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.notesapp.MainViewModel

@Composable
fun StartScreen (navHostController: NavHostController, viewModel: MainViewModel) {
    Scaffold() {
        Text(text = "Hello World!")
    }
}