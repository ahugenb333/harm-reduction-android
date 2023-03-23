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
import org.joda.time.LocalDate
import com.ahugenb.hra.R

//TODO this is WIP to confirm quick actions work. Will be completed on branch 'tracker'
@Composable
fun DayView(trackerViewModel: TrackerViewModel) {
    val weekDay = LocalDate.now().dayOfWeek.toShortWeekDay()
    val state = trackerViewModel.trackerState.collectAsState().value
    var drinks = 0.0
    var cravings = 0

    when(state) {
        is TrackerState.TrackerStateAll -> {
            drinks = state.today.drinks
            cravings = state.today.cravings
        }
        else -> { }
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
            Text(text = stringResource(id = R.string.hra_drinks, drinks))
            Text(text = stringResource(id = R.string.hra_cravings, cravings))
            Button(
                onClick = {
                    trackerViewModel.updateDrinks(drinks + 1)
                }
            ) {
                Text(text = stringResource(id = R.string.hra_drink))
            }
            Button(
                onClick = {
                    trackerViewModel.updateCravings(cravings + 1)
                }
            ) {
                Text(text = stringResource(id = R.string.hra_craving))
            }
        }
    }
}
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