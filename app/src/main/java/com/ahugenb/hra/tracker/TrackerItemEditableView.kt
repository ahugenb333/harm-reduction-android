package com.ahugenb.hra.tracker

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R
import com.ahugenb.hra.Utils.Companion.acceptCravingsText
import com.ahugenb.hra.Utils.Companion.acceptDollarsText
import com.ahugenb.hra.Utils.Companion.acceptDrinksText
import com.ahugenb.hra.Utils.Companion.prettyPrintShort
import com.ahugenb.hra.Utils.Companion.roundedToTwo
import com.ahugenb.hra.Utils.Companion.smartToDouble
import com.ahugenb.hra.Utils.Companion.smartToInt
import com.ahugenb.hra.tracker.db.Day
import java.util.Locale

@Composable
fun TrackerItemEditableView(
    day: Day, // Use the passed 'day' directly
    onUpdateDay: (Day) -> Unit // Callback to update the day
) {
    // Initialize states from the passed 'day' using TextFieldValue
    val initialDrinksStr = day.drinks.roundedToTwo().toString()
    val drinksTfv = remember(day.id) {
        mutableStateOf(TextFieldValue(text = initialDrinksStr, selection = TextRange(initialDrinksStr.length)))
    }
    val initialPlannedStr = day.planned.toString()
    val plannedTfv = remember(day.id) {
        mutableStateOf(TextFieldValue(text = initialPlannedStr, selection = TextRange(initialPlannedStr.length)))
    }
    val initialCravingsStr = day.cravings.toString()
    val cravingsTfv = remember(day.id) {
        mutableStateOf(TextFieldValue(text = initialCravingsStr, selection = TextRange(initialCravingsStr.length)))
    }
    val initialMoneyStr = String.format(Locale.getDefault(), "%.2f", day.moneySpent)
    val moneyTfv = remember(day.id) {
        mutableStateOf(TextFieldValue(text = initialMoneyStr, selection = TextRange(initialMoneyStr.length)))
    }
    val initialNotesStr = day.notes
    val notesTfv = remember(day.id) {
        mutableStateOf(TextFieldValue(text = initialNotesStr, selection = TextRange(initialNotesStr.length)))
    }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current // For Toast

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //drinks
            Text(
                text = stringResource(id = R.string.hra_tracker_drinks),
                modifier = Modifier.weight(0.5f, true)
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.3f, true)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            val currentText = drinksTfv.value.text
                            if (currentText == "0.0" || currentText == "0") {
                                drinksTfv.value = TextFieldValue("")
                            } else {
                                drinksTfv.value = drinksTfv.value.copy(selection = TextRange(currentText.length))
                            }
                        }
                    },
                value = drinksTfv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = { newValue ->
                    if (newValue.text.acceptDrinksText()) {
                        drinksTfv.value = newValue
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //planned
            Text(
                text = stringResource(id = R.string.hra_tracker_planned),
                modifier = Modifier.weight(0.5f, true)
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.3f, true)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            val currentText = plannedTfv.value.text
                            if (currentText == "0.0" || currentText == "0") {
                                plannedTfv.value = TextFieldValue("")
                            } else {
                                plannedTfv.value = plannedTfv.value.copy(selection = TextRange(currentText.length))
                            }
                        }
                    },
                value = plannedTfv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = { newValue ->
                    if (newValue.text.acceptDrinksText()) {
                        plannedTfv.value = newValue
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //cravings
            Text(
                text = stringResource(id = R.string.hra_tracker_cravings),
                modifier = Modifier.weight(0.5f, true)
                    .padding(start = 24.dp, end = 20.dp, top = 8.dp),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.3f, true)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            val currentText = cravingsTfv.value.text
                            if (currentText == "0") { // Cravings are Int
                                cravingsTfv.value = TextFieldValue("")
                            } else {
                                cravingsTfv.value = cravingsTfv.value.copy(selection = TextRange(currentText.length))
                            }
                        }
                    },
                value = cravingsTfv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, // Kept Decimal for flexibility, validation handles Int
                    imeAction = ImeAction.Next),
                onValueChange = { newValue ->
                    if (newValue.text.acceptCravingsText()) {
                        cravingsTfv.value = newValue
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //money
            Text(
                text = stringResource(id = R.string.hra_tracker_money),
                modifier = Modifier.weight(0.5f, true)
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.3f, true)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            val currentText = moneyTfv.value.text
                            if (currentText == "0.00" || currentText == "0.0" || currentText == "0") {
                                moneyTfv.value = TextFieldValue(text = "", selection = TextRange(0))
                            } else {
                                moneyTfv.value = moneyTfv.value.copy(selection = TextRange(currentText.length))
                            }
                        }
                    },
                value = moneyTfv.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = { newValue ->
                    if (newValue.text.acceptDollarsText()) {
                        moneyTfv.value = newValue
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //notes
            Text(
                text = stringResource(id = R.string.hra_tracker_notes),
                modifier = Modifier.weight(0.5f, true)
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp),
                style = MaterialTheme.typography.h6,
                maxLines = 1,
            )
            OutlinedTextField(
                maxLines = 3,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(0.5f, true)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            notesTfv.value = notesTfv.value.copy(selection = TextRange(notesTfv.value.text.length))
                        }
                    },
                value = notesTfv.value,
                onValueChange = { newValue ->
                    notesTfv.value = newValue
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),
                label = { },
                placeholder = {  Text(text = stringResource(id = R.string.notes_placeholder)) }
            )
        }
        Row {
            Spacer(modifier = Modifier.fillMaxWidth(.67f))
            Button(
                onClick = {
                    val newDay = day.copy(
                        drinks = drinksTfv.value.text.smartToDouble(),
                        planned = plannedTfv.value.text.smartToDouble(),
                        cravings = cravingsTfv.value.text.smartToInt(),
                        moneySpent = moneyTfv.value.text.removePrefix("$").smartToDouble(),
                        notes = notesTfv.value.text
                    )
                    onUpdateDay(newDay) // Call the lambda with the updated day
                    focusManager.clearFocus()
                    Toast.makeText(context, stringResource(id = R.string.day_updated_toast, newDay.prettyPrintShort()),
                        Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp)
            ) {
                Text(text = stringResource(R.string.hra_update))
            }
        }
    }

}