package com.ahugenb.hra.tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.Utils.Companion.getClosestMonday
import com.ahugenb.hra.Utils.Companion.isToday
import com.ahugenb.hra.Utils.Companion.prettyPrint
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerView(viewModel: TrackerViewModel) {
    val trackerState = viewModel.trackerState.collectAsState().value
    val isDropdownExpanded = remember { mutableStateOf(false) }

    val weekList = when (trackerState) {
        is TrackerState.TrackerStateAll -> viewModel.getWeekOf(trackerState.today)
        else -> listOf()
    }

    val beginningsList = when (trackerState) {
        is TrackerState.TrackerStateAll -> viewModel.getWeekBeginnings()
        else -> listOf()
    }

    val today = when (trackerState) {
        is TrackerState.TrackerStateAll -> trackerState.today
        else -> Day()
    }

    val daysOfWeek = remember { mutableStateOf(weekList) }
    val weekBeginnings = remember { mutableStateOf(beginningsList) }
    //todo week summary header

    val selectedOptionText = remember {
        mutableStateOf(beginningsList.getClosestMonday(today).prettyPrint())
    }
    val selectedIndex = remember { mutableStateOf(0) }

    Column {

        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colors.onSurface,
            LocalContentAlpha provides ContentAlpha.high
        ) {
            OutlinedTextField(
                value = selectedOptionText.value,
                maxLines = 1,
                enabled = false,
                label = { Text(text = "Week Beginning:") },
                onValueChange = { },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(0.5f)
                    .padding(8.dp)
                    .clickable { isDropdownExpanded.value = !isDropdownExpanded.value },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown menu icon"
                    )
                }
            )
        }

        DropdownMenu(
            expanded = isDropdownExpanded.value,
            onDismissRequest = { isDropdownExpanded.value = false },
            modifier = Modifier.fillMaxWidth(0.5f),
        ) {
            weekBeginnings.value.forEachIndexed { i, it ->
                DropdownMenuItem(onClick = {
                    selectedIndex.value = i
                    isDropdownExpanded.value = false
                    selectedOptionText.value = it.prettyPrint()
                }) {
                    val todayText = if (it.isToday()) " (Today)" else ""
                    Text(text = it.prettyPrint() + todayText)
                }

            }
        }

        LazyColumn {
            daysOfWeek.value.forEachIndexed { i, it ->
                item(key = i, content = {
                    TrackerItemView(it)
                })
            }
        }
    }
}