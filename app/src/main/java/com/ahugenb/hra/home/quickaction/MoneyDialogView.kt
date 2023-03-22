package com.ahugenb.hra.home.quickaction

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ahugenb.hra.Format.Companion.isSanitized
import com.ahugenb.hra.Format.Companion.isSanitizedDollars
import com.ahugenb.hra.Format.Companion.isValidDollars
import com.ahugenb.hra.Format.Companion.smartToDouble
import com.ahugenb.hra.R


@Composable
fun MoneyDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    val textValue = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.hra_spent))
        },
        text = {
            OutlinedTextField(
                value = textValue.value,
                onValueChange = {
                    if (it.isSanitizedDollars() && it.smartToDouble().isValidDollars()) {
                        textValue.value = it
                    }
                },
                label = { Text(text = "Enter dollar amount") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier,
                keyboardActions = KeyboardActions(
                    onDone = {
                        val value = textValue.value.smartToDouble()
                        onConfirm(value)
                        onDismiss()
                    }
                ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val value = textValue.value.smartToDouble()
                    onConfirm(value)
                    onDismiss()
                }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        }
    )
}
