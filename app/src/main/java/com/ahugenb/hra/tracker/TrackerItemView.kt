package com.ahugenb.hra.tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R
import com.ahugenb.hra.Utils.Companion.isToday
import com.ahugenb.hra.Utils.Companion.prettyPrintLong
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerItemView(day: Day, viewModel: TrackerViewModel) {
    val state = viewModel.trackerState.collectAsState().value as TrackerState.TrackerStateAll
    val showExpanded = day.id == state.selectedDay?.id
    val ic = if (showExpanded) R.drawable.ic_caret_down else R.drawable.ic_caret_right

    val text =
        if (day.isToday())
            day.prettyPrintLong().plus(" - Today")
        else
            day.prettyPrintLong()
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            if (showExpanded) {
                viewModel.setSelectedDay(null)
            } else {
                viewModel.setSelectedDay(day)
            }
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 4.dp),
            text = AnnotatedString(text),
            style = MaterialTheme.typography.h6,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 128.dp),
                painter = painterResource(ic),
                contentDescription = "Expandable menu icon"
            )
        }
    }
    if (showExpanded) {
        TrackerItemEditableView(day, viewModel)
    }
    Divider(modifier = Modifier.height(2.dp))
}