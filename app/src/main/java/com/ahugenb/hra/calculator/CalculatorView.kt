package com.ahugenb.hra.calculator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.R

@Composable
fun CalculatorView(navController: NavController, viewModel: CalculatorViewModel) {
    val volume = remember { mutableStateOf("") }
    val abv = remember { mutableStateOf("") }
    val drinks = remember { mutableStateOf("1.0") }

    BackHandler(enabled = true) {
        viewModel.clear()
        navController.navigateUp()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier.weight(1f, true),
                value = volume.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    if (it.isSanitized() && it.smartToDouble().isValidVolume()) {
                        volume.value = it
                        viewModel.updateCalculation(
                            abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble()
                        )
                    }
                },
                label = { Text(text = "fl Oz") }
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier.weight(1f, true),
                value = abv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    if (it.isSanitized() && it.smartToDouble().isValidPercent()) {
                        abv.value = it
                        viewModel.updateCalculation(
                            abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble()
                        )
                    }
                },
                label = { Text(text = "% ABV") }
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier.weight(1f, true),
                value = drinks.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    if (it.isSanitized() && it.smartToDouble().isValidDrinks()) {
                        drinks.value = it
                        viewModel.updateCalculation(
                            abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble()
                        )
                    }
                },
                label = { Text(text = "No. of Drinks") }
            )
        }
        val units = viewModel.units.collectAsState().value
        val ozPureEthanol = viewModel.ozPureEthanol.collectAsState().value
        Text(
            textAlign = TextAlign.Right,
            text = stringResource(R.string.hra_units, units), style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
        )
        Text(
            textAlign = TextAlign.Right,
            text = stringResource(R.string.hra_ethanol, ozPureEthanol),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
        )
    }
}

/*
* sanitized input:
*  -is less than 10 digits in length
*  -may not contain more than one decimal
*  -may only contain digits from validInput
*/
private fun String.isSanitized(): Boolean {
    val validInput = "1234567890."

    return (this.isEmpty() || this.length < 10 && this.count { ch -> ch == '.' } < 2
            && this.all { ch -> validInput.contains(ch) })
}

private fun Double.isValidPercent(): Boolean = this in 0.0..100.0

private fun Double.isValidDrinks(): Boolean = this in 0.0..10000.0

private fun Double.isValidVolume(): Boolean = this in 0.0..100000.0

private fun String.smartToDouble(): Double =
    when (this.isEmpty()) {
        true -> 0.0
        else -> this.toDouble()
    }