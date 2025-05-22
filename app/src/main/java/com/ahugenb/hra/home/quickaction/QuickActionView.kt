package com.ahugenb.hra.home.quickaction

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R

@Composable
fun QuickActionView(
    todayDrinks: Double,
    todayCravings: Int,
    todayMoneySpent: Double, // Though not directly used in buttons, it's for context if MoneyDialog was more complex
    todayPlannedDrinks: Double,
    onAddMoneySpent: (Double) -> Double,    // returns new total money spent
    onUpdateDrinks: (Double) -> Double,     // returns new total drinks
    onUpdateCravings: (Int) -> Int          // returns new total cravings
) {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog.value) {
        MoneyDialogView(
            onDismiss = { showDialog.value = false },
            onConfirm = { amount ->
                showDialog.value = false
                if (amount > 0) {
                    val newTotalMoneySpent = onAddMoneySpent(amount)
                    Toast.makeText(context, context.getString(R.string.hra_money_spent_today, newTotalMoneySpent),
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
                val updatedDrinks = onUpdateDrinks(todayDrinks + 0.5)
                Toast.makeText(context,
                    context.getString(R.string.hra_drinks_today, updatedDrinks, todayPlannedDrinks),
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
                val updatedDrinks = onUpdateDrinks(todayDrinks + 1.0)
                Toast.makeText(context,
                    context.getString(R.string.hra_drinks_today, updatedDrinks, todayPlannedDrinks),
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
                val updatedCravings = onUpdateCravings(todayCravings + 1)
                Toast.makeText(context, context.getString(R.string.hra_cravings_today, updatedCravings),
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