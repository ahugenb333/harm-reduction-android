package com.ahugenb.hra.calculator

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.Utils.Companion.isSanitized
import com.ahugenb.hra.Utils.Companion.isValidDrinks
import com.ahugenb.hra.Utils.Companion.isValidPercent
import com.ahugenb.hra.Utils.Companion.isValidVolume
import com.ahugenb.hra.Utils.Companion.smartToDouble
import com.ahugenb.hra.R

@Composable
fun CalculatorView(viewModel: CalculatorViewModel, navController: NavController) {
    val focusManager = LocalFocusManager.current
    val volume = remember { mutableStateOf("") }
    val abv = remember { mutableStateOf("") }
    val drinks = remember { mutableStateOf("1.0") }
    val mlChecked = remember { mutableStateOf(false) }
    val labelId = if (mlChecked.value) R.string.hra_ml else R.string.hra_oz
    val ethanolId = if (mlChecked.value) R.string.hra_ethanol_ml else R.string.hra_ethanol_oz

    BackHandler(enabled = true) {
        viewModel.clear()
        navController.navigateUp()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier.weight(1f, true).padding(end = 8.dp),
                value = volume.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptVolumeText()) {
                        volume.value = it
                        viewModel.updateCalculation(
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
                modifier = Modifier.weight(1f, true).padding(end = 8.dp),
                value = abv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptPercentText()) {
                        abv.value = it
                        viewModel.updateCalculation(
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                onValueChange = {
                    if (it.acceptDrinksText()) {
                        drinks.value = it
                        viewModel.updateCalculation(
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
        val units = viewModel.calculatorState.collectAsState().value.units
        val pureEthanol = viewModel.calculatorState.collectAsState().value.ethanol
        Card(elevation = 10.dp, modifier = Modifier.padding(8.dp)) {
            Column {
                Text(
                    textAlign = TextAlign.Right,
                    text = stringResource(R.string.hra_units, units), style = MaterialTheme.typography.h4,
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
        Text(
            text = stringResource(labelId),
            modifier = Modifier.padding(end = 16.dp),
            textAlign = TextAlign.Left
        )
        Switch(
            modifier = Modifier.padding(end = 12.dp),
            checked = mlChecked.value,
            onCheckedChange = {
                focusManager.clearFocus()
                mlChecked.value = it

                viewModel.updateCalculation(
                    abv.value.smartToDouble(),
                    volume.value.smartToDouble(),
                    drinks.value.smartToDouble(),
                    mlChecked.value
                )
            }
        )
    }
}

private fun String.acceptPercentText(): Boolean =
    this.isSanitized() && this.smartToDouble().isValidPercent()

private fun String.acceptDrinksText(): Boolean =
    this.isSanitized() && this.smartToDouble().isValidDrinks()

private fun String.acceptVolumeText(): Boolean =
    this.isSanitized() && this.smartToDouble().isValidVolume()