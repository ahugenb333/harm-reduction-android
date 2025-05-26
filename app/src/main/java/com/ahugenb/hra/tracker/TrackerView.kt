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
import androidx.compose.ui.res.stringResource // Added import
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.R // Added import for R
import com.ahugenb.hra.Utils.Companion.prettyPrintShort
import com.ahugenb.hra.tracker.db.Day // Ensure Day is imported

@Composable
fun TrackerView(
    trackerState: TrackerState, // State passed directly
    navController: NavController,
    onUpdateSelectedMonday: (Int) -> Unit,
    onSetSelectedDay: (Day?) -> Unit,
    onNavigateBack: () -> Boolean,
    getLastWeek: () -> List<Day>, // To get data for TrackerHeaderView
    onUpdateDay: (Day) -> Unit // For TrackerItemEditableView
) {
    // Handle empty state or cast to the expected state
    val currentTrackerState = when (trackerState) { // Use passed trackerState
        is TrackerState.TrackerStateEmpty -> {
            return // Potentially show a loading indicator or an empty state message
        }
        is TrackerState.TrackerStateAll -> trackerState // Use passed trackerState
    }

    val isDropdownExpanded = remember { mutableStateOf(false) }

    val weekBeginnings = currentTrackerState.weekBeginnings
    val selectedMonday = currentTrackerState.selectedMonday
    val daysOfWeek = currentTrackerState.daysOfWeek
    val selectedOptionText = selectedMonday.prettyPrintShort()

    BackHandler(enabled = true) {
        val weekWasReset = onNavigateBack() // Use passed lambda
        if (!weekWasReset) {
            navController.navigateUp()
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(end = 8.dp, top = 8.dp)
        ) {
            TrackerHeaderView(
                currentDaysOfWeek = daysOfWeek,
                lastWeekDays = getLastWeek() // Use passed lambda
            )
            Column {
                OutlinedTextField(
                    value = selectedOptionText,
                    maxLines = 1,
                    enabled = false,
                    label = { Text(text = stringResource(id = R.string.tracker_week_beginning_label)) },
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDropdownExpanded.value = !isDropdownExpanded.value
                        },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = stringResource(id = R.string.dropdown_menu_icon_cd)
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
                    weekBeginnings.forEachIndexed { i, dayRef -> // Renamed 'it' to 'dayRef'
                        DropdownMenuItem(onClick = {
                            isDropdownExpanded.value = false
                            onUpdateSelectedMonday(i) // Use passed lambda
                        }) {
                            Text(text = dayRef.prettyPrintShort())
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
                            onSetSelectedDay(if (isSelected) null else day) // Use passed lambda
                        },
                        editableContent = {
                            if (isSelected) {
                                TrackerItemEditableView(
                                    day = day,
                                    onUpdateDay = { updatedDay ->
                                        onUpdateDay(updatedDay) // Use passed lambda
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