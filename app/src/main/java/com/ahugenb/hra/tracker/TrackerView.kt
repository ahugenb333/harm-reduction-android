package com.ahugenb.hra.tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.Utils.Companion.getClosestMonday
import com.ahugenb.hra.Utils.Companion.prettyPrintLong
import com.ahugenb.hra.Utils.Companion.prettyPrintShort
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerView(viewModel: TrackerViewModel) {
    val trackerState = viewModel.trackerState.collectAsState().value
    val isDropdownExpanded = remember { mutableStateOf(false) }

    val weekList = when (trackerState) {
        is TrackerState.TrackerStateAll -> viewModel.getWeekOf(trackerState.today)
        else -> listOf()
    }

    val beginningsList = viewModel.getWeekBeginnings()

    val today = when (trackerState) {
        is TrackerState.TrackerStateAll -> trackerState.today
        else -> Day()
    }

    val daysOfWeek = remember { mutableStateOf(weekList) }
    val weekBeginnings = remember { mutableStateOf(beginningsList) }
    //todo week summary header

    val selectedOptionText = remember {
        mutableStateOf(beginningsList.getClosestMonday(today).prettyPrintShort())
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDropdownExpanded.value = !isDropdownExpanded.value },
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
                    TrackerItemView(it)
                })
            }
        }
    }
}