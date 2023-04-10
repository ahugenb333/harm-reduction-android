package com.ahugenb.hra

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionView(viewModel: WearViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ){
            Button(onClick = { viewModel.sendHalfDrink() }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_wear_half_drink))
            }
            Button(onClick = { viewModel.sendWholeDrink() }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_wear_full_drink))
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Button(onClick = { viewModel.sendCraving() }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_wear_craving))
            }
            Button(
                onClick = {
                    //todo alertDialog
                    viewModel.sendMoneySpent()
                }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp)
            ) {
                Text(text = stringResource(id = R.string.hra_wear_money))
            }
        }
    }
}