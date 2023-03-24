package com.ahugenb.hra.tracker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.Utils.Companion.prettyPrint
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerItemView(day: Day) {
    val showExpanded = remember { mutableStateOf(false) }

    ClickableText(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 4.dp)
        .fillMaxWidth(),
        text = AnnotatedString(day.prettyPrint()),
        style = MaterialTheme.typography.h6,
        onClick = {
            showExpanded.value = !showExpanded.value
        }
    )
    if (showExpanded.value) {
        //todo editable expanded section, rotating caret
    }
    Divider(modifier = Modifier.height(2.dp))
}