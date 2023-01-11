package com.example.notesapp.Views.SubView

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.*
import com.example.notesapp.R


@Composable
fun DatePicker(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateSelected: (Date) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selDate = remember { mutableStateOf(Calendar.getInstance().time) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(color = colorResource(id = R.color.very_dark_grey))
        ) {
            CustomCalendarView(
                minDate,
                maxDate,
                onDateSelected = {
                        newDate ->
                    selDate.value = newDate;
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                IconButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = stringResource(R.string.reject_title),
                        color = colorResource(R.color.white)
                    )
                }

                IconButton(
                    onClick = {
                        val newDate = selDate.value
                        onDateSelected(
                            Date(
                                maxOf(
                                    minOf(maxDate ?: Long.MAX_VALUE, newDate.time),
                                    minDate ?: Long.MIN_VALUE
                                )
                            )
                        )
                        onDismissRequest()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.admit_title),
                        color = colorResource(R.color.purple_200)
                    )
                }

            }
        }
    }
}

@Composable
private fun CustomCalendarView(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateSelected: (Date) -> Unit
) {
    AndroidView(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = colorResource(id = R.color.very_dark_grey)
            ),
        factory = { context ->
            CalendarView(context)
        },
        update = { view ->
            view.dateTextAppearance = R.style.CalenderViewDateCustomText
            view.firstDayOfWeek = R.style.CalenderViewDateCustomText
            view.weekDayTextAppearance = R.style.CalenderViewDateCustomText

            if (minDate != null)
                view.minDate = minDate
            if (maxDate != null)
                view.maxDate = maxDate

            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    Calendar
                        .getInstance()
                        .apply {
                            set(year, month, dayOfMonth)
                        }
                        .time
                )
            }
        }
    )
}