package com.example.notesapp.Views

import android.app.Application
import android.provider.Telephony.Carriers.PASSWORD
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StartScreen (navHostController: NavHostController, viewModel: MainViewModel) {

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var remTitle by remember { mutableStateOf("") }
    var remDesc by remember { mutableStateOf("") }

    Scaffold(
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
    }

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
                    OutlinedTextField(
                        value = remDesc,
                        onValueChange = { remDesc = it },
                        label = { Text(text = stringResource(R.string.new_deskription)) }
                    )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            // TODO viewModel добавитьь в базу данных
                            // что-то типа viewModel.initDatabase(TYPE_FIREBASE) {}
                        },
                        enabled = remTitle.isNotEmpty()
                    ) {
//                        Text(text = Constants.Keys.SIGN_IN)
                    }
                }
            }
        }
    ) {}
}




@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    NotesAppTheme() {
        val context = LocalContext.current
        val mViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context.applicationContext as Application))
        StartScreen(navHostController = rememberNavController(), viewModel = mViewModel)
    }
}