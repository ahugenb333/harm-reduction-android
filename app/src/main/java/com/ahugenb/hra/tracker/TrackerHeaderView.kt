package com.ahugenb.hra.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.Utils.Companion.getCravingsTotal
import com.ahugenb.hra.Utils.Companion.getDrinksTotal
import com.ahugenb.hra.Utils.Companion.getMoneySpentTotal
import com.ahugenb.hra.Utils.Companion.getPlannedTotal
import com.ahugenb.hra.Utils.Companion.roundedToTwo
import java.util.Locale

@Composable
fun TrackerHeaderView(viewModel: TrackerViewModel) {
    val daysOfWeek = (viewModel.trackerState.collectAsState().value as TrackerState.TrackerStateAll)
        .daysOfWeek

    val drinks = daysOfWeek.getDrinksTotal().roundedToTwo()
    val planned = daysOfWeek.getPlannedTotal().roundedToTwo()
    val cravings = daysOfWeek.getCravingsTotal()
    val money = daysOfWeek.getMoneySpentTotal()

    val lastWeek = viewModel.getLastWeek()
    val drinksLastWeek = lastWeek.getDrinksTotal().roundedToTwo()
    val cravingsLastWeek = lastWeek.getCravingsTotal()
    val moneyLastWeek = lastWeek.getMoneySpentTotal()

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