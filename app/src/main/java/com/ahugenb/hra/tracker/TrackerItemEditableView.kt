package com.ahugenb.hra.tracker

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.R
import com.ahugenb.hra.tracker.db.Day

@Composable
fun TrackerItemEditableView(day: Day, viewModel: TrackerViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            //drinks

        }
        Row {
            //planned
        }
        Row {
            //cravings
        }
        Row {
            //money spent
        }
        Row {
            //notes
        }
        Row {
            Spacer(modifier = Modifier.fillMaxWidth(.67f))
            Button(
                onClick = { viewModel.updateDay(day) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, end = 8.dp)
            ) {
                Text(text = stringResource(R.string.hra_update))
            }
        }
    }

}