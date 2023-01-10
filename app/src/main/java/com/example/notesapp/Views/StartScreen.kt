package com.example.notesapp.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notesapp.MainViewModel
import com.example.notesapp.R
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.DpOffset
import com.example.notesapp.Models.ListModel
import com.example.notesapp.Models.NoteModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun StartScreen (navHostController: NavHostController, viewModel: MainViewModel) {

    // Переменные заметки
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var remTitle by remember { mutableStateOf("") }
    var remDesc by remember { mutableStateOf("") }
    var isDescShowed by remember { mutableStateOf(false) }
    var isChoosen by remember { mutableStateOf(false) }

    // Списки во viewPager
    val pagerState = rememberPagerState()
    val pages = listOf(ListModel(name = ""), ListModel(name = stringResource(R.string.main_task_list))) +
            viewModel.readAllLists().observeAsState(listOf()).value

    // Добавление списка
    val bottomSheetStateForList = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var remListTitle by remember { mutableStateOf("") }
    var remIsANewList by remember { mutableStateOf(true) }



    // Основной блок
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.task_title),
                        )
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.padding(end = 20.dp)
                            ) {
                                Icon(Icons.Default.MoreVert, contentDescription = "open_menu")
                            }

                            // Выпадающий список настроек
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(x = 20.dp, y = 10.dp)
                            ) {
                                if (pagerState.currentPage > 1) {
                                    DropdownMenuItem(onClick = {
                                        remIsANewList = false
                                        coroutineScope.launch {
                                            remListTitle = pages[pagerState.currentPage].name
                                            bottomSheetStateForList.show()
                                        }
                                    }) { Text(stringResource(R.string.rename_list)) }

                                    DropdownMenuItem(onClick = {
                                        coroutineScope.launch {
                                            pagerState.scrollToPage(pagerState.currentPage - 1)
                                            viewModel.deleteList(pages[pagerState.currentPage + 1]) {}
                                        }
                                    }) { Text(stringResource(R.string.delete_list)) }
                                } else {
                                    // Фальшивые кнопки
                                    DropdownMenuItem(onClick = {  }) { Text(stringResource(R.string.rename_list), color = colorResource(
                                        id = R.color.grey)) }
                                    DropdownMenuItem(onClick = { }) { Text(stringResource(R.string.delete_list), color = colorResource(
                                        id = R.color.grey)) }
                                }

                                Divider()
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    remIsANewList = true
                                    coroutineScope.launch {
                                        bottomSheetStateForList.show()
                                    }
                                }) { Text(stringResource(R.string.new_list)) }
                            }
                        }
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 12.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    bottomSheetState.show()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "EditAction",
                    tint = colorResource(R.color.purple_500)
                )
            }
        }
    ) {
        Column() {

            // ViewPager
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                pages.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(title.name)
                            if (title.name == "") {
                                Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = colorResource(
                                    id = R.color.purple_200
                                ))
                            } },
                        selected = pagerState.currentPage == index,
                        onClick = {
                              coroutineScope.launch {
                                  pagerState.scrollToPage(index)
                              }
                        },
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState,
            ) { page ->
                // TODO: page content
            }
        }
    }





    // Добавление заметки
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetElevation = 10.dp,
        sheetShape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp),
        sheetContent = {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 32.dp)
                ) {
                    OutlinedTextField(
                        value = remTitle,
                        onValueChange = { remTitle = it },
                        label = { Text(text = stringResource(R.string.new_task)) },
                        isError = remTitle.isEmpty()
                    )
                    if (isDescShowed) {
                        OutlinedTextField(
                            value = remDesc,
                            onValueChange = { remDesc = it },
                            label = { Text(text = stringResource(R.string.new_deskription)) }
                        )
                    }

                    // Нижняя панель кнопок
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        Button(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            onClick = {
                                isDescShowed = !isDescShowed
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                        }

                        Button(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            onClick = {
                                // TODO Выбор даты
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
                        }

                        Button(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            onClick = {
                                isChoosen = !isChoosen
                            }
                        ) {
                            if (isChoosen) {
                                Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = colorResource(
                                    id = R.color.purple_200
                                ))
                            } else {
                                Icon(imageVector = Icons.Outlined.Star, contentDescription = "")
                            }
                        }

                        Button(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            onClick = {
                                 viewModel.createNote(
                                     pages[pagerState.currentPage].firebaseId,
                                     NoteModel(
                                         title = remTitle,
                                         description = remDesc,
                                         // TODO внятное время
                                         choosen = isChoosen,
                                         done = false
                                         // TODO возможно, добавить id родителя
                                     )
                                 ) {}
                            },
                            enabled = remTitle.isNotEmpty()
                        ) {
                            if (remTitle.isEmpty()) {
                                Text(text = stringResource(R.string.save), color = colorResource(id = R.color.grey))
                            } else {
                                Text(text = stringResource(R.string.save), color = colorResource(id = R.color.purple_200))
                            }
                        }
                    }

                }
            }
        }
    ) {}


    // Добавление списка
    ModalBottomSheetLayout(
        sheetState = bottomSheetStateForList,
        sheetElevation = 10.dp,
        sheetShape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp),
        sheetContent = {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 32.dp)
                ) {
                    OutlinedTextField(
                        value = remListTitle,
                        onValueChange = { remListTitle = it },
                        label = { Text(text = stringResource(R.string.new_list)) },
                        isError = remListTitle.isEmpty()
                    )

                    // Сохранение
                    Button(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        onClick = {
                            if (remIsANewList) {
                                viewModel.createList(ListModel(name = remListTitle)) {}
                            } else {
                                viewModel.updateList(ListModel(firebaseId = pages[pagerState.currentPage].firebaseId, name = remListTitle)) {}
                            }
                            remListTitle = ""
                            coroutineScope.launch {
                                bottomSheetStateForList.hide()
                            }
                        },
                        enabled = remListTitle.isNotEmpty()
                    ) {
                        if (remListTitle.isEmpty()) {
                            Text(text = stringResource(R.string.save), color = colorResource(id = R.color.grey))
                        } else {
                            Text(text = stringResource(R.string.save), color = colorResource(id = R.color.purple_200))
                        }
                    }

                }
            }
        }
    ) {}
}
