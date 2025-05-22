package com.ahugenb.hra.calculator

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.Utils.Companion.smartToDouble
import com.ahugenb.hra.R
import com.ahugenb.hra.Utils.Companion.acceptDrinksText
import com.ahugenb.hra.Utils.Companion.acceptPercentText
import com.ahugenb.hra.Utils.Companion.acceptVolumeText
import com.ahugenb.hra.Utils.Companion.roundedToTwo
// TrackerViewModel and TrackerState are no longer directly needed here for this specific part.

@Composable
fun CalculatorView(
    calculatorViewModel: CalculatorViewModel,
    navController: NavController,
    onAddCalculatedUnitsToDay: (units: Double) -> Double, // Returns new total drinks
    plannedDrinksToday: Double // To keep Toast message detailed
) {
    val volume = remember { mutableStateOf("") }
    val abv = remember { mutableStateOf("") }
    val drinks = remember { mutableStateOf("1.0") }
    val mlChecked = remember { mutableStateOf(false) }
    val labelId = if (mlChecked.value) R.string.hra_ml else R.string.hra_oz
    val ethanolId = if (mlChecked.value) R.string.hra_ethanol_ml else R.string.hra_ethanol_oz

    val calculatorState = calculatorViewModel.calculatorState.collectAsState().value
    val units = calculatorState.units
    val pureEthanol = calculatorState.ethanol

    // trackerState is no longer collected here directly for this view's primary purpose.
    // Information needed from it (like plannedDrinksToday) is passed in.

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    BackHandler(enabled = true) {
        calculatorViewModel.onClear() // Use lambda
        navController.navigateUp()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.End
    ) {
        Card(elevation = 10.dp, modifier = Modifier.padding(8.dp)) {
            Column {
                Text(
                    textAlign = TextAlign.Right,
                    text = stringResource(R.string.hra_units, units),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                )
                Text(
                    textAlign = TextAlign.Right,
                    text = stringResource(ethanolId, pureEthanol),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(end = 8.dp),
                value = volume.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptVolumeText()) {
                        volume.value = it
                        calculatorViewModel.onUpdateCalculation( // Use lambda
                            abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble(),
                            mlChecked.value
                        )
                    }
                },
                label = { Text(text = stringResource(labelId)) }
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(end = 8.dp),
                value = abv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptPercentText()) {
                        abv.value = it
                        calculatorViewModel.onUpdateCalculation( // Use lambda
                            abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble(),
                            mlChecked.value
                        )
                    }
                },
                label = { Text(text = "% ABV") }
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier.weight(1f, true),
                value = drinks.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                onValueChange = {
                    if (it.acceptDrinksText()) {
                        drinks.value = it
                        calculatorViewModel.onUpdateCalculation( // Use lambda
                            abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble(),
                            mlChecked.value
                        )
                    }
                },
                label = { Text(text = "No. of Drinks") },
            )
        }
        Text(
            text = stringResource(labelId),
            modifier = Modifier.padding(end = 16.dp),
            textAlign = TextAlign.Center
        )
        Switch(
            modifier = Modifier.padding(end = 12.dp),
            checked = mlChecked.value,
            onCheckedChange = {
                focusManager.clearFocus()
                mlChecked.value = it

                calculatorViewModel.onUpdateCalculation( // Use lambda
                    abv.value.smartToDouble(),
                    volume.value.smartToDouble(),
                    drinks.value.smartToDouble(),
                    mlChecked.value
                )
            }
        )
        Button(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                val currentUnits = calculatorState.units.roundedToTwo()
                val newTotalDrinks = onAddCalculatedUnitsToDay(currentUnits)
                Toast.makeText(context, context.getString(R.string.hra_drinks_today,
                    newTotalDrinks, plannedDrinksToday), Toast.LENGTH_SHORT)
                    .show()
            },
        ) {
            Text(text = stringResource(R.string.hra_add_today))
        }
    }
}