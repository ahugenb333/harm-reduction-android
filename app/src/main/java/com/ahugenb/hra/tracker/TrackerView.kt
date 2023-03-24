package com.ahugenb.hra.tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.Utils.Companion.prettyPrintShort

@Composable
fun TrackerView(viewModel: TrackerViewModel) {
    val trackerState = viewModel.trackerState.collectAsState().value as TrackerState.TrackerStateAll
    val isDropdownExpanded = remember { mutableStateOf(false) }

    val weekBeginnings = remember { mutableStateOf(trackerState.weekBeginnings) }
    val selectedDay = remember { mutableStateOf(trackerState.selectedDay) }
    val selectedMonday = remember {
        mutableStateOf(trackerState.selectedMonday)
    }
    val daysOfWeek = remember { mutableStateOf(trackerState.daysOfWeek) }


    //todo week summary header

    val selectedOptionText = remember {
        mutableStateOf(selectedMonday.value.prettyPrintShort())
    }
    val selectedIndex = remember { mutableStateOf(0) }

    Column {
        Row(
            modifier = Modifier.padding(end = 8.dp, top = 8.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(0.5f))
            Column {
                OutlinedTextField(
                    value = selectedOptionText.value,
                    maxLines = 1,
                    enabled = false,
                    label = { Text(text = "Week Beginning:") },
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDropdownExpanded.value = !isDropdownExpanded.value
                            viewModel.updateSelectedMonday(selectedIndex.value)
                                   },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown menu icon"
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Black,
                        disabledTrailingIconColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )
                DropdownMenu(
                    expanded = isDropdownExpanded.value,
                    onDismissRequest = { isDropdownExpanded.value = false },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    weekBeginnings.value.forEachIndexed { i, it ->
                        DropdownMenuItem(onClick = {
                            selectedIndex.value = i
                            isDropdownExpanded.value = false
                            viewModel.updateSelectedDay(it)
                            selectedMonday.value = it
                            selectedOptionText.value = it.prettyPrintShort()
                        }) {
                            Text(text = it.prettyPrintShort())
                        }
                    }
                }
            }
        }
        LazyColumn {
            daysOfWeek.value.forEachIndexed { i, it ->
                item(key = i, content = {
                    TrackerItemView(it, viewModel)
                })
            }
        }
    }
}