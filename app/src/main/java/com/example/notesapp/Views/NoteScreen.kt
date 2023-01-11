package com.example.notesapp.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesapp.MainViewModel
import com.example.notesapp.Models.NoteModel
import com.example.notesapp.Navigation.NavRoute
import com.example.notesapp.R


@Composable
fun NoteScreen (navHostController: NavHostController, viewModel: MainViewModel, parentId: String?, noteId: String?) {
    val note = viewModel.readNotesWithParent(parentId!!).observeAsState(listOf()).value.firstOrNull({it.firebaseId == noteId})
    if (note == null) {
        return
    }

    var remTitle by remember { mutableStateOf(note.title) }
    var remDesc by remember { mutableStateOf(note.description) }
    var remTime by remember { mutableStateOf(note.time) }
    var remChoosen by remember { mutableStateOf(note.choosen) }
    var remDone by remember { mutableStateOf(note.done) }

    val subnotes = viewModel.readNotesWithParent(note.firebaseId).observeAsState(listOf()).value

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
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
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

            Spacer(modifier = Modifier.height(10.dp))
            
            
            // Добавить описание
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
                ) {
                Icon(Icons.Filled.Edit, contentDescription = "add_a_description")
                TextField(
                    value = remDesc,
                    colors = TextFieldDefaults.textFieldColors(
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

            // Добавить время
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .clickable {
                        // TODO выбор времени
                    },
                verticalAlignment = Alignment.CenterVertically
                ) {
                Icon(Icons.Filled.DateRange, contentDescription = "add_a_description")
                TextField(
                    value = if (remTime.isEmpty()) {
                        stringResource(id = R.string.add_time)
                    } else {
                        remTime
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    readOnly = true,
                    onValueChange = {}
                )
            }

//            Добавить подзадачу
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_round_subdirectory_arrow_right_24),
                        contentDescription = "add_a_description"
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn {
                        items(subnotes.sortedBy { it.done }) { note ->
                            GetSubCard(noteModel = note, viewModel = viewModel)
                        }
                    }
                    Spacer(modifier = Modifier.padding(start = 10.dp, top = 10.dp))
                    Text(
                        modifier = Modifier
                            .clickable {
                                viewModel.createNote(note.firebaseId, NoteModel(title = "", parent = note.firebaseId, time = "")) {}
                            },
                        text = stringResource(id = R.string.add_subtask),
                    )
                }
            }
        }
    }
}


@Composable
fun GetSubCard(noteModel: NoteModel, viewModel: MainViewModel) {
    var remDone = noteModel.done
    var remChos = noteModel.choosen

    var remTitle by remember { mutableStateOf(noteModel.title) }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                remDone = !remDone
                viewModel.updateNote(
                    noteModel.parent,
                    NoteModel(
                        firebaseId = noteModel.firebaseId,
                        title = noteModel.title,
                        description = noteModel.description,
                        time = noteModel.time,
                        choosen = remChos,
                        done = remDone,
                        parent = noteModel.parent
                    )
                ) {}
            },
        ) {
            val iconId = if (remDone) {
                R.drawable.ic_baseline_lens_24
            } else {
                R.drawable.ic_outline_lens_24
            }
            Icon(
                painterResource(id = (iconId)),
                contentDescription = "open_menu"
            )
        }


        TextField(
            modifier = Modifier.width(220.dp),
            value = remTitle,
            placeholder = { Text(stringResource(id = R.string.add_name)) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                remTitle = it
                viewModel.updateNote(
                    noteModel.parent,
                    NoteModel(
                        firebaseId = noteModel.firebaseId,
                        title = remTitle,
                        description = noteModel.description,
                        time = noteModel.time,
                        choosen = noteModel.choosen,
                        done = noteModel.done,
                        parent = noteModel.parent
                    )
                ) {}
            }
        )


        IconButton(
            onClick = {
                remChos = !remChos
                viewModel.updateNote(
                    noteModel.parent,
                    NoteModel(
                        firebaseId = noteModel.firebaseId,
                        title = noteModel.title,
                        description = noteModel.description,
                        time = noteModel.time,
                        choosen = remChos,
                        done = remDone,
                        parent = noteModel.parent
                    )
                ) {}
            },
        ) {
            if (remChos) {
                Icon(Icons.Outlined.Star, contentDescription = "open_menu", tint = colorResource(
                    id = R.color.purple_200
                ))
            } else {
                Icon(Icons.Filled.Star, contentDescription = "open_menu")
            }
        }

        IconButton(
            onClick = {
                remChos = !remChos
                viewModel.deleteNote(noteModel.parent, noteModel) {}
            },
        ) {
            Icon(painterResource(R.drawable.ic_round_close_24), contentDescription = "delete", tint = colorResource(
                    id = R.color.purple_200))
        }
    }
}