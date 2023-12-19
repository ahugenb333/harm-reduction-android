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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Drinks", style = MaterialTheme.typography.h6)
                RadioButton(selected = unit == GoalUnit.DRINKS, onClick = { unit = GoalUnit.DRINKS })
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Cravings", style = MaterialTheme.typography.h6)
                RadioButton(selected = unit == GoalUnit.CRAVINGS, onClick = { unit = GoalUnit.CRAVINGS })
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Money", style = MaterialTheme.typography.h6)
                RadioButton(selected = unit == GoalUnit.MONEY, onClick = { unit = GoalUnit.MONEY })
            }
        }
    }
}