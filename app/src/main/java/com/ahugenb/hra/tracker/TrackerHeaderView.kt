package com.ahugenb.hra.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.tracker.db.Day // Added import for Day
import com.ahugenb.hra.Utils.Companion.getCravingsTotal
import com.ahugenb.hra.Utils.Companion.getDrinksTotal
import com.ahugenb.hra.Utils.Companion.getMoneySpentTotal
import com.ahugenb.hra.Utils.Companion.getPlannedTotal
import com.ahugenb.hra.Utils.Companion.roundedToTwo
import java.util.Locale

@Composable
fun TrackerHeaderView(
    currentDaysOfWeek: List<Day>,
    lastWeekDays: List<Day>
) {
    val drinks = currentDaysOfWeek.getDrinksTotal().roundedToTwo()
    val planned = currentDaysOfWeek.getPlannedTotal().roundedToTwo()
    val cravings = currentDaysOfWeek.getCravingsTotal()
    val money = currentDaysOfWeek.getMoneySpentTotal()

    val drinksLastWeek = lastWeekDays.getDrinksTotal().roundedToTwo()
    val cravingsLastWeek = lastWeekDays.getCravingsTotal()
    val moneyLastWeek = lastWeekDays.getMoneySpentTotal()

    Card(elevation = 10.dp, modifier = Modifier.padding(4.dp).fillMaxWidth(0.5f)) {
        Column(
            modifier = Modifier.padding(4.dp)
        ){
            Text(
                text = "Drinks: $drinks\r\n($drinksLastWeek last week, $planned planned)",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Cravings: $cravings\r\n($cravingsLastWeek last week)",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = String.format(Locale.getDefault(), "Money: $%.2f\r\n($%.2f last week)", money, moneyLastWeek),
                style = MaterialTheme.typography.body2
            )
        }
    }
}