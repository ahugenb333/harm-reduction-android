package com.ahugenb.hra.home.export

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ahugenb.hra.AuthClickListener
import com.ahugenb.hra.R

@Composable
fun ExportButton(listener: AuthClickListener) {
    Button(
        onClick = { listener.onAuthClicked() }
    ) {
        Text(
            text = stringResource(R.string.hra_export),
            style = MaterialTheme.typography.h6
        )
    }
}