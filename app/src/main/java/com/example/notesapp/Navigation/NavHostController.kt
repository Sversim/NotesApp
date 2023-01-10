package com.example.notesapp.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.MainViewModel
import com.example.notesapp.Views.EmptyScreen
import com.example.notesapp.Views.NoteScreen
import com.example.notesapp.Views.StartScreen

sealed class NavRoute(val route: String) {
    object EmptyScreen: NavRoute("empty_screen");
    object StartScreen: NavRoute("start_screen");
    object NoteScreen: NavRoute("note_screen");
}

@Composable
fun NavHostController(mViewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.EmptyScreen.route) {
        composable(NavRoute.EmptyScreen.route) {EmptyScreen(navHostController = navController, viewModel = mViewModel)}
        composable(NavRoute.StartScreen.route) {StartScreen(navHostController = navController, viewModel = mViewModel)}
        composable(NavRoute.NoteScreen.route + "/{Id}") {
                backStackEntry ->
            NoteScreen(
                navHostController = navController,
                viewModel = mViewModel,
                noteId = backStackEntry.arguments?.getString("Id")
            )
        }
    }
}