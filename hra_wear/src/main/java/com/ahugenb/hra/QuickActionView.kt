package com.ahugenb.hra

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionView(viewModel: WearViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ){
            Button(
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = Color(0xFF670A0A),
                        contentColor = Color(0xFFE5E5E5)
                    ),
                onClick = { viewModel.sendHalfDrink() }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_wear_half_drink))
            }
            Button(
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = Color(0xFF670A0A),
                        contentColor = Color(0xFFE5E5E5)
                    ),
                onClick = { viewModel.sendWholeDrink() }, modifier = Modifier
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
            Button(
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = Color(0xFF670A0A),
                        contentColor = Color(0xFFE5E5E5)
                    ),
                onClick = { viewModel.sendCraving() }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_wear_craving))
            }
            Button(
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = Color(0xFF670A0A),
                        contentColor = Color(0xFFE5E5E5)
                    ),
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