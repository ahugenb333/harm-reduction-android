package com.ahugenb.hra.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.tracker.TrackerViewModel
import org.joda.time.LocalDate

@Composable
fun TodayView(navController: NavController, trackerViewModel: TrackerViewModel) {
    val weekDay = LocalDate.now().dayOfWeek.toShortWeekDay()
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(8.dp).fillMaxWidth().clickable { }
    ) {
        Text(text = weekDay, modifier = Modifier.padding(16.dp))
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