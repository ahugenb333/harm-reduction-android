package com.ahugenb.hra.home.quickaction

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R
import com.ahugenb.hra.tracker.TrackerState
import com.ahugenb.hra.tracker.TrackerViewModel

@Composable
fun QuickActionView(trackerViewModel: TrackerViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    val state = trackerViewModel.trackerState.collectAsState().value
    var drinks = 0.0
    var cravings = 0

    when (state) {
        is TrackerState.TrackerStateDay -> {
            drinks = state.day.drinks
            cravings = state.day.cravings
        }
        else -> {}
    }

    if (showDialog.value) {
        MoneyDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { showDialog.value = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                trackerViewModel.updateDrinks(drinks + 1)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.hra_drink),
                style = MaterialTheme.typography.h6
            )
        }
        Button(
            onClick = {
                trackerViewModel.updateCravings(cravings + 1)
            },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                text = stringResource(id = R.string.hra_craving),
                style = MaterialTheme.typography.h6
            )
        }
        Button(
            onClick = {
                showDialog.value = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.hra_bought),
                style = MaterialTheme.typography.h6
            )
        }
    }
}