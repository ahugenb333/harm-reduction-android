package com.ahugenb.hra.tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.joda.time.LocalDate
import com.ahugenb.hra.R
import com.ahugenb.hra.tracker.db.Day


@Composable
fun DayView(navController: NavController, trackerViewModel: TrackerViewModel) {
    val weekDay = LocalDate.now().dayOfWeek.toShortWeekDay()
    val state = trackerViewModel.trackerState.collectAsState().value
    val id = "26/02/2022"

    val days = when(state) {
        is TrackerState.TrackerStateDays -> state.days
        else -> emptyList()
    }
    var todayNullable = days.find {
       it.id == "26/02/2022"
    }

    val today = when(todayNullable) {
        null -> Day(id, 0.0, 0.0, 0, 0.0, "")
        else -> todayNullable
    }

    Card(
        elevation = 10.dp,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { }
    ) {
        Column {
            Text(text = weekDay, modifier = Modifier.padding(16.dp))
            Text(text = stringResource(id = R.string.hra_drinks, today.drinks))
            Text(text = stringResource(id = R.string.hra_cravings, today.cravings))
            Button(
                onClick = {
                    val updated = today.copy(drinks = today.drinks + 1)
                    trackerViewModel.updateDays(listOf(updated))
                }
            ) {
                Text(text = stringResource(id = R.string.hra_drink))
            }
            Button(
                onClick = {
                    val updated = today.copy(cravings = today.cravings + 1)
                    trackerViewModel.updateDays(listOf(updated))
                }
            ) {
                Text(text = stringResource(id = R.string.hra_craving))
            }
        }
    }
}

//todo: editable todayview (reuse for planner/tracker)

private fun Int.toShortWeekDay(): String =
    when(this) {
        1 -> "M"
        2 -> "T"
        3 -> "W"
        4 -> "Th"
        5 -> "F"
        6 -> "Sa"
        7 -> "Su"
        else -> ""
    }