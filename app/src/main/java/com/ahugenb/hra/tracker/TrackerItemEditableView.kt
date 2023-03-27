package com.ahugenb.hra.tracker

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
import com.ahugenb.hra.R
import com.ahugenb.hra.Utils.Companion.isSanitizedDecimal
import com.ahugenb.hra.Utils.Companion.isSanitizedDollars
import com.ahugenb.hra.Utils.Companion.isValidCravings
import com.ahugenb.hra.Utils.Companion.isValidDollars
import com.ahugenb.hra.Utils.Companion.isValidDrinks
import com.ahugenb.hra.Utils.Companion.smartToDouble
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerItemEditableView(day: Day, viewModel: TrackerViewModel) {
    val selectedDay = (viewModel.trackerState.collectAsState().value as TrackerState.TrackerStateAll)
        .selectedDay ?: return
    val drinks = remember { mutableStateOf(selectedDay.drinks.toString()) }
    val planned = remember { mutableStateOf(selectedDay.planned.toString()) }
    val cravings = remember { mutableStateOf(selectedDay.cravings.toString()) }
    val money = remember { mutableStateOf(selectedDay.moneySpent.toString()) }
    val notes = remember { mutableStateOf(selectedDay.notes) }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            Text(
                text = stringResource(id = R.string.hra_tracker_drinks),
                modifier = Modifier.weight(0.25f, true),
                style = MaterialTheme.typography.h6
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.25f, true)
                    .padding(end = 8.dp),
                value = drinks.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.isSanitizedDecimal() && it.smartToDouble().isValidDrinks()) {
                        drinks.value = it
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            Text(
                text = stringResource(id = R.string.hra_tracker_planned),
                modifier = Modifier.weight(0.25f, true),
                style = MaterialTheme.typography.h6
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.25f, true)
                    .padding(end = 8.dp),
                value = planned.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.isSanitizedDecimal() && it.smartToDouble().isValidDrinks()) {
                        planned.value = it
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            Text(
                text = stringResource(id = R.string.hra_tracker_cravings),
                modifier = Modifier.weight(0.25f, true),
                style = MaterialTheme.typography.h6
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.25f, true)
                    .padding(end = 8.dp),
                value = cravings.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.isSanitizedDecimal() && it.toInt().isValidCravings()) {
                        cravings.value = it
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            Text(
                text = stringResource(id = R.string.hra_tracker_money),
                modifier = Modifier.weight(0.25f, true),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            OutlinedTextField(
                maxLines = 1,
                modifier = Modifier
                    .weight(0.25f, true)
                    .padding(end = 8.dp),
                value = money.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.isSanitizedDollars() && it.smartToDouble().isValidDollars()) {
                        money.value = it
                    }
                },
                label = { },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            Text(
                text = stringResource(id = R.string.hra_tracker_notes),
                modifier = Modifier.weight(0.25f, true),
                style = MaterialTheme.typography.h6,
            )
            OutlinedTextField(
                maxLines = 3,
                modifier = Modifier
                    .weight(0.5f, true)
                    .padding(end = 8.dp),
                value = notes.value,
                onValueChange = {
                    notes.value = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),
                label = { },
                placeholder = {  Text(text = "Notes") }
            )
        }
        Row {
            Spacer(modifier = Modifier.fillMaxWidth(.67f))
            Button(
                onClick = {
                    val newDay = day.copy(
                        drinks = drinks.value.smartToDouble(),
                        planned = planned.value.smartToDouble(),
                        cravings = cravings.value.toInt(),
                        moneySpent = money.value.smartToDouble(),
                        notes = notes.value
                    )
                    viewModel.updateDay(newDay)
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