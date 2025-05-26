package com.ahugenb.hra.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource // Added import
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R
import com.ahugenb.hra.Utils.Companion.isToday
import com.ahugenb.hra.Utils.Companion.prettyPrintLong
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerItemView(
    day: Day,
    isSelected: Boolean,
    onToggleSelected: () -> Unit,
    editableContent: @Composable () -> Unit // Slot for TrackerItemEditableView
) {
    val ic = if (isSelected) R.drawable.ic_caret_down else R.drawable.ic_caret_right
    val prettyPrintedDay = day.prettyPrintLong()
    val text =
        if (day.isToday())
            stringResource(id = R.string.tracker_item_today_suffix, prettyPrintedDay)
        else
            prettyPrintedDay

    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onToggleSelected // Use the passed lambda
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 4.dp),
            text = AnnotatedString(text),
            style = MaterialTheme.typography.h6,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp),
                painter = painterResource(ic),
                contentDescription = stringResource(id = R.string.expandable_menu_icon_cd)
            )
        }
    }
    if (isSelected) {
        editableContent() // Render the editable content if selected
    }
    Divider(thickness = 1.dp)
}