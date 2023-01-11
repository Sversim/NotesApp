package com.example.notesapp.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.notesapp.MainViewModel
import com.example.notesapp.Models.NoteModel
import com.example.notesapp.Navigation.NavRoute
import com.example.notesapp.R
import kotlinx.coroutines.launch


@Composable
fun NoteScreen (navHostController: NavHostController, viewModel: MainViewModel, parentId: String?, noteId: String?) {
    val note = viewModel.readNotesWithParent(parentId!!).observeAsState(listOf()).value.firstOrNull()
    if (note == null) {
        return
    }

//    val note = viewModel.readNotesWithParent(parentId).observeAsState(listOf()).value.first()
    var remTitle by remember { mutableStateOf(note.title) }
    var remDesc by remember { mutableStateOf(note.description) }
    var remTime by remember { mutableStateOf(note.time) }
    var remChoosen by remember { mutableStateOf(note.choosen) }
    var remDone by remember { mutableStateOf(note.done) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceBetween
             ) {
                 IconButton(onClick = {
                     navHostController.navigate(NavRoute.StartScreen.route)
                 }) {
                     Icon(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24), contentDescription = "arrow_back")
                 }

                 IconButton(onClick = {
                     remChoosen = !remChoosen
                     viewModel.updateNote(
                         parentId,
                         NoteModel(
                             firebaseId = note.firebaseId,
                             title = note.title,
                             description = note.description,
                             time = note.time,
                             choosen = remChoosen,
                             done = note.done,
                             parent = note.parent
                         )
                     ) {}
                 }) {
                     if (remChoosen) {
                         Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = colorResource(
                             id = R.color.purple_200
                            )
                         )
                     } else {
                         Icon(imageVector = Icons.Outlined.Star, contentDescription = "")
                     }
                 }

                 IconButton(onClick = {
                     viewModel.deleteNote(parentId, note) {}
                     navHostController.navigate(NavRoute.StartScreen.route)
                 }) {
                     Icon(painter = painterResource(id = R.drawable.ic_outline_delete_24), contentDescription = "delete")
                 }
             }
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = if (remDone) {
                    colorResource(R.color.purple_200)
                } else {
                    colorResource(R.color.dark_grey)
                },
                onClick = {
                    remDone = !remDone
                    viewModel.updateNote(
                        parentId,
                        NoteModel(
                            firebaseId = note.firebaseId,
                            title = note.title,
                            description = note.description,
                            time = note.time,
                            choosen = note.choosen,
                            done = remDone,
                            parent = note.parent
                        )
                    ) {}
                    if (remDone) {
                        navHostController.navigate(NavRoute.StartScreen.route)
                    }
            }) {
                Icon(
                    painterResource(id = R.drawable.ic_round_check_24),
                    contentDescription = "EditAction",
                    tint = if (!remDone) {
                        colorResource(R.color.purple_200)
                    } else {
                        colorResource(R.color.dark_grey)
                    }
                )
            }
        }
    ) {

    }
}