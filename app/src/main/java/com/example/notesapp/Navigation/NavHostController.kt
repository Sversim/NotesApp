package com.example.notesapp.Navigation

import androidx.compose.runtime.Composable

sealed class NavRoute(val route: String) {
    object StartScreen: NavRoute("start_screen");
    object NoteScreen: NavRoute("note_screen");
}

@Composable
fun NavHostController() {
}