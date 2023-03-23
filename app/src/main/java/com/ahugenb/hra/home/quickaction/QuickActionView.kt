package com.ahugenb.hra.home.quickaction

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R
import com.ahugenb.hra.tracker.TrackerState
import com.ahugenb.hra.tracker.TrackerViewModel

@Composable
fun QuickActionView(viewModel: TrackerViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    val state = viewModel.trackerState.collectAsState().value
    val context = LocalContext.current
    var drinks = 0.0
    var cravings = 0
    var moneySpent = 0.0

    when (state) {
        is TrackerState.TrackerStateAll -> {
            drinks = state.today.drinks
            cravings = state.today.cravings
            moneySpent = state.today.moneySpent
        }
        else -> {}
    }

    if (showDialog.value) {
        MoneyDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { showDialog.value = false
                if (it > 0) {
                    val newMoneySpent = moneySpent + it
                    viewModel.updateMoneySpent(newMoneySpent)
                    Toast.makeText(context, context.getString(R.string.hra_money_spent_today, newMoneySpent),
                        Toast.LENGTH_SHORT).show()
                }
            }
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
                val newDrinks = drinks + 0.5
                viewModel.updateDrinks(newDrinks)
                Toast.makeText(context, context.getString(R.string.hra_drinks_today, newDrinks),
                    Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hra_half_drink),
                style = MaterialTheme.typography.h6
            )
        }
        Button(
            onClick = {
                val newDrinks = ++drinks
                viewModel.updateDrinks(newDrinks)
                Toast.makeText(context, context.getString(R.string.hra_drinks_today, newDrinks),
                    Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hra_drink),
                style = MaterialTheme.typography.h6
            )
        }
        Button(
            onClick = {
                val newCravings = ++cravings
                viewModel.updateCravings(newCravings)
                Toast.makeText(context, context.getString(R.string.hra_cravings_today, newCravings),
                    Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)

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
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hra_bought),
                style = MaterialTheme.typography.h6
            )
        }
    }
}