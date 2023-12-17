package com.ahugenb.hra.goal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.Utils.Companion.acceptDrinksText
import com.ahugenb.hra.Utils.Companion.smartToDouble
import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalImpl
import com.ahugenb.hra.goal.db.GoalPeriod
import com.ahugenb.hra.goal.db.GoalUnit
import java.util.UUID

@Composable
fun GoalDetailScreen(goalToEdit: Goal? = null) {
    var red by remember { mutableStateOf(goalToEdit?.red.toString() ?: "") }
    var yellow by remember { mutableStateOf(goalToEdit?.yellow.toString() ?: "") }
    var green by remember { mutableStateOf(goalToEdit?.green.toString() ?: "") }
    var period by remember { mutableStateOf(goalToEdit?.period ?: GoalPeriod.DAILY) }
    var unit by remember { mutableStateOf(goalToEdit?.unit ?: GoalUnit.DRINKS) }
    var enableYellowField by remember { mutableStateOf(goalToEdit?.isYellowType() == true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = red,
            onValueChange = {
                if (it.acceptDrinksText()) {
                    red = it
                }
            },
            label = { Text("Red") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Handle Next button press if needed */ }
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Enable Yellow Field")
            Switch(
                checked = enableYellowField,
                onCheckedChange = { enableYellowField = it },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        if (enableYellowField) {
            OutlinedTextField(
                value = yellow,
                onValueChange = {
                    if (it.acceptDrinksText()) {
                        yellow = it
                    }
                },
                label = { Text("Yellow") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Handle Next button press if needed */ }
                )
            )
        }

        OutlinedTextField(
            value = green,
            onValueChange = {
                if (it.acceptDrinksText()) {
                    green = it
                }
            },
            label = { Text("Green") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Handle Next button press if needed */ }
            )
        )



        Button(
            onClick = {
                val editedGoal = GoalImpl(
                    goalId = goalToEdit?.goalId ?: UUID.randomUUID(),
                    red = red.smartToDouble(),
                    yellow = yellow.smartToDouble(),
                    green = green.smartToDouble(),
                    period = period,
                    unit = unit
                )

                if (editedGoal.isSavable()) {
                    // Handle saving the edited goal, e.g., update the goal in the database
                } else {
                    // Handle case where the edited goal is not savable
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(if (goalToEdit != null) "Save Changes" else "Save Goal")
        }
    }
}