package com.ahugenb.hra.calculator

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.navigation.NavScreen

@Composable
fun CalculatorView(viewModel: CalculatorViewModel) {
    val volume = remember { mutableStateOf("0.0") }
    val abv = remember { mutableStateOf("0.0") }
    val drinks = remember { mutableStateOf("1.0") }

    Column(modifier = Modifier.fillMaxHeight(),
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
                    if (it.isSanitized()) {
                        volume.value = it
                        viewModel.updateUnits(abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble())
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
                        viewModel.updateUnits(abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble())
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
                    if (it.isSanitized()) {
                        drinks.value = it
                        viewModel.updateUnits(abv.value.smartToDouble(),
                            volume.value.smartToDouble(),
                            drinks.value.smartToDouble())
                    }
                },
                label = { Text(text = "No. of Drinks") }
            )
        }
        val units = viewModel.units.collectAsState().value
        val ozPureEthanol = viewModel.ozPureEthanol.collectAsState().value
        Text(text = units, style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = ozPureEthanol, style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(horizontal = 8.dp))
    }
}

//sanitized input must not contain more than one decimal and may only contain digits from validInput
private fun String.isSanitized(): Boolean {
    val validInput = "1234567890."

    return (this.isEmpty() || this.count { ch -> ch == '.' }  < 2
            && (this.all { ch -> validInput.contains(ch) }))
}

private fun Double.isValidPercent(): Boolean = this in 0.0..100.0

private fun String.smartToDouble(): Double =
    when(this.isEmpty()) {
        true -> 0.0
        else -> this.toDouble()
    }