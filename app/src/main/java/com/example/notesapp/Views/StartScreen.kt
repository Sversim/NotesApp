package com.example.notesapp.Views

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.MainViewModel
import com.example.notesapp.MainViewModelFactory
import com.example.notesapp.R
import com.example.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
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


    // Основное блок
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.task_title),
                        modifier = Modifier.fillMaxWidth()
                    )
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

            }
        }
    ) {
        Column() {
            val pagerState = rememberPagerState()
            val pages = viewModel.readAllLists().observeAsState(listOf()).value

//            TabRow(
//                // Our selected tab is our current page
//                selectedTabIndex = pagerState.currentPage,
//                // Override the indicator, using the provided pagerTabIndicatorOffset modifier
//                indicator = { tabPositions ->
//                    TabRowDefaults.Indicator(
//                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
//                    )
//                }
//            ) {
//                // Add tabs for all of our pages
//                pages.forEachIndexed { index, title ->
//                    Tab(
//                        text = { Text(title.name) },
//                        selected = pagerState.currentPage == index,
//                        onClick = { /* TODO */ },
//                    )
//                }
//            }
//
//            HorizontalPager(
//                count = pages.size,
//                state = pagerState,
//            ) { page ->
//                // TODO: page content
//            }
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
                            modifier = Modifier.padding(top = 16.dp).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            onClick = {
                                // TODO Выбор даты
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
                        }

                        Button(
                            modifier = Modifier.padding(top = 16.dp).fillMaxHeight(),
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
                            modifier = Modifier.padding(top = 16.dp).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            onClick = {
                                // TODO viewModel добавить в базу данных
                                // что-то типа viewModel.initDatabase(TYPE_FIREBASE) {}
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
}
