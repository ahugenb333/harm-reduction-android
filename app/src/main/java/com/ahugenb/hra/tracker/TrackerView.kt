package com.ahugenb.hra.tracker

import androidx.activity.compose.BackHandler
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
import androidx.navigation.NavController
import com.ahugenb.hra.Utils.Companion.prettyPrintShort

@Composable
fun TrackerView(viewModel: TrackerViewModel, navController: NavController) {
    val trackerState = viewModel.trackerState.collectAsState().value as TrackerState.TrackerStateAll
    val isDropdownExpanded = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

    val weekBeginnings = trackerState.weekBeginnings
    val selectedMonday = trackerState.selectedMonday
    val daysOfWeek = trackerState.daysOfWeek
    val selectedOptionText = selectedMonday.prettyPrintShort()

    BackHandler(enabled = true) {
        viewModel.updateSelectedMonday(0)
        navController.navigateUp()
    }

    Column {
        Row(
            modifier = Modifier.padding(end = 8.dp, top = 8.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(0.5f))
            Column {
                OutlinedTextField(
                    value = selectedOptionText,
                    maxLines = 1,
                    enabled = false,
                    label = { Text(text = "Week Beginning:") },
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDropdownExpanded.value = !isDropdownExpanded.value
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
                    weekBeginnings.forEachIndexed { i, it ->
                        DropdownMenuItem(onClick = {
                            isDropdownExpanded.value = false
                            viewModel.updateSelectedMonday(i)
                            if (selectedIndex.value != i) {
                                viewModel.setSelectedDay(null)
                                selectedIndex.value = i
                            }
                        }) {
                            Text(text = it.prettyPrintShort())
                        }
                    }
                }
            }
        }
        LazyColumn {
            daysOfWeek.forEachIndexed { i, it ->
                item(key = i, content = {
                    TrackerItemView(it, viewModel)
                })
            }
        }
    }
}