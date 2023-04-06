package com.ahugenb.hra_wear

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ){
            Button(onClick = { }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_half_drink))
            }
            Button(onClick = { }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_full_drink))
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Button(onClick = { }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp)
            ) {
                Text(text = stringResource(R.string.hra_craving))
            }
            Button(onClick = { }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp)
            ) {
                Text(text = stringResource(id = R.string.hra_bought))
            }
        }
    }
}