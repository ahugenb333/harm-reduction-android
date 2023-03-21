package com.ahugenb.hra.quickaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    when(state) {
        is TrackerState.TrackerStateDay -> {
            drinks = state.day.drinks
            cravings = state.day.cravings
        }
        else -> { }
    }

//    when(showDialog.value) {
//        MoneyDialog(
//            onDismiss = { showDialog.value = false },
//            onConfirm = { showDialog.value = false },
//            viewModel = trackerViewModel
//        )
//    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
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
        Button(
            onClick = {
                showDialog.value = true
            }
        ) {
            Text(text = stringResource(id = R.string.hra_bought))
        }
    }
}