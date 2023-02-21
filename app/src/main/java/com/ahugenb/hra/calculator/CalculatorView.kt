package com.ahugenb.hra.calculator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CalculatorView(navController: NavController, viewModel: CalculatorViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val volume = remember { mutableStateOf("") }
        val abv = remember { mutableStateOf("") }
        val drinks = remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier.weight(1f, true),
            value = volume.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                if (it.isSanitized()) {
                    volume.value = it
                }
            },
            label = { Text(text = "Oz") }
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f, true),
            value = abv.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                if (it.isSanitized()) {
                    abv.value = it
                }
            },
            label = { Text(text = "% ABV") }
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f, true),
            value = drinks.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                if (it.isSanitized()) {
                    drinks.value = it
                }
            },
            label = { Text(text = "No. of Drinks") }
        )
    }
}

//sanitized input must not contain more than one decimal and may only contain digits from validInput
private fun String.isSanitized(): Boolean {
    val validInput = "1234567890."

    return (this.count { ch -> ch == '.' }  < 2
            && (this.isEmpty() || this.all { ch -> validInput.contains(ch) }))
}