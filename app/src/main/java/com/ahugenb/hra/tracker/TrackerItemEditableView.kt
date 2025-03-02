package com.ahugenb.hra.tracker

import android.widget.Toast
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
fun TrackerItemEditableView(day: Day, viewModel: TrackerViewModel) {
    val selectedDay = (viewModel.trackerState.collectAsState().value as TrackerState.TrackerStateAll)
        .selectedDay ?: return
    val drinks = remember { mutableStateOf(selectedDay.drinks.roundedToTwo().toString()) }
    val planned = remember { mutableStateOf(selectedDay.planned.toString()) }
    val cravings = remember { mutableStateOf(selectedDay.cravings.toString()) }
    val money = remember { mutableStateOf(String.format(Locale.getDefault(),"%.2f", selectedDay.moneySpent)) }
    val notes = remember { mutableStateOf(selectedDay.notes) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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
                    .weight(0.3f, true),
                value = drinks.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptDrinksText()) {
                        drinks.value = it
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
                modifier = Modifier.weight(0.3f, true),
                value = planned.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptDrinksText()) {
                        planned.value = it
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
                    .weight(0.3f, true),
                value = cravings.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptCravingsText()) {
                        cravings.value = it
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
                    .weight(0.3f, true),
                value = money.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                onValueChange = {
                    if (it.acceptDollarsText()) {
                        money.value = it
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
                    .weight(0.5f, true),
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
                        cravings = cravings.value.smartToInt(),
                        moneySpent = money.value.removePrefix("$").smartToDouble(),
                        notes = notes.value
                    )
                    viewModel.updateDay(newDay)
                    focusManager.clearFocus()
                    Toast.makeText(context,newDay.prettyPrintShort() + " updated",
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