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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.Utils.Companion.prettyPrintShort

@Composable
fun TrackerView(viewModel: TrackerViewModel, navController: NavController) {
    val trackerStateValue = viewModel.trackerState.collectAsState().value // Renamed for clarity

    // Handle empty state or cast to the expected state
    val currentTrackerState = when (trackerStateValue) {
        is TrackerState.TrackerStateEmpty -> {
            // Potentially show a loading indicator or an empty state message
            return
        }
        is TrackerState.TrackerStateAll -> trackerStateValue
    }

    val isDropdownExpanded = remember { mutableStateOf(false) }
    // selectedIndex is not strictly needed from ViewModel's perspective anymore,
    // as ViewModel now manages selectedMonday directly via index.
    // However, it was used to clear selectedDay, which is now handled by onUpdateSelectedMonday lambda.

    val weekBeginnings = currentTrackerState.weekBeginnings
    val selectedMonday = currentTrackerState.selectedMonday
    val daysOfWeek = currentTrackerState.daysOfWeek
    val selectedOptionText = selectedMonday.prettyPrintShort()

    // Use the ViewModel's onNavigateBack lambda
    BackHandler(enabled = true) {
        val weekWasReset = viewModel.onNavigateBack()
        if (!weekWasReset) {
            navController.navigateUp() // Perform default back navigation if not handled by ViewModel
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(end = 8.dp, top = 8.dp)
        ) {
            TrackerHeaderView(
                currentDaysOfWeek = daysOfWeek,
                lastWeekDays = viewModel.getLastWeek() // Called here and result passed
            )
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
                        disabledTextColor = MaterialTheme.colors.onSurface,
                        disabledTrailingIconColor = MaterialTheme.colors.onSurface,
                        disabledLabelColor = MaterialTheme.colors.onSurface
                            .copy(alpha = ContentAlpha.high),
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
                            viewModel.onUpdateSelectedMonday(i)
                            // selectedDay is reset within onUpdateSelectedMonday in ViewModel
                        }) {
                            Text(text = it.prettyPrintShort())
                        }
                    }
                }
                Spacer(modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth(1f))
            }
        }
        Spacer(modifier = Modifier
            .height(8.dp)
            .fillMaxWidth(1f))
        LazyColumn {
            daysOfWeek.forEachIndexed { _, day -> // Index 'i' not used, replaced with _
                val isSelected = day.id == currentTrackerState.selectedDay?.id
                item(key = day.id, content = {
                    TrackerItemView(
                        day = day,
                        isSelected = isSelected,
                        onToggleSelected = {
                            viewModel.onSetSelectedDay(if (isSelected) null else day)
                        },
                        editableContent = {
                            if (isSelected) { // Only compose if actually selected
                                TrackerItemEditableView(
                                    day = day,
                                    onUpdateDay = { updatedDay ->
                                        viewModel.updateDay(updatedDay)
                                    }
                                )
                            }
                        }
                    )
                })
            }
        }
    }
}