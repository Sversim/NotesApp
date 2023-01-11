package com.example.notesapp.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesapp.MainViewModel
import com.example.notesapp.Models.NoteModel
import com.example.notesapp.Navigation.NavRoute
import com.example.notesapp.R


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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = remTitle,
                textStyle = TextStyle.Default.copy(fontSize = 28.sp),
                onValueChange = {
                    remTitle = it
                    if (!remTitle.isEmpty()) {
                        viewModel.updateNote(
                            parentId,
                            NoteModel(
                                firebaseId = note.firebaseId,
                                title = remTitle,
                                description = note.description,
                                time = note.time,
                                choosen = note.choosen,
                                done = note.done,
                                parent = note.parent
                            )
                        ) {}
                    }
                }
            )
            
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                onClick = { /*TODO Добавить описание*/ }) {
                Icon(Icons.Filled.Edit, contentDescription = "add_a_description")
                TextField(
                    value = remDesc,
                    colors = TextFieldDefaults.textFieldColors(
//                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        remDesc = it
                        viewModel.updateNote(
                            parentId,
                            NoteModel(
                                firebaseId = note.firebaseId,
                                title = note.title,
                                description = remDesc,
                                time = note.time,
                                choosen = note.choosen,
                                done = note.done,
                                parent = note.parent
                            )
                        ) {}
                    })
            }
        }
    }
}