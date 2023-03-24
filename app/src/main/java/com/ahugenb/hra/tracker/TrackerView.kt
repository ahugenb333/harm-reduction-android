package com.ahugenb.hra.tracker

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun TrackerView(viewModel: TrackerViewModel) {
    val trackerState = viewModel.trackerState.collectAsState().value

    val thisWeek = when (trackerState) {
        is TrackerState.TrackerStateAll -> viewModel.getWeekOf(trackerState.today)
        else -> listOf()
    }

    //todo week summary header

    LazyColumn {
        thisWeek.forEachIndexed { i, it ->
            item(key = i, content = {
                TrackerItemView(it)
            })
        }
    }
}