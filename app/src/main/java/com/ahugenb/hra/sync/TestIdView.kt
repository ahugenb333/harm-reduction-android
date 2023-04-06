package com.ahugenb.hra.sync

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext

@Composable
fun TestIdView(viewModel: SyncViewModel) {
    val state = viewModel.firebaseInfo.collectAsState().value
    if (state is SyncState.SyncStateAll) {
        Text(text = state.firebaseInfo.id.toString())
    } else {
        viewModel.getFirebaseInfo()
    }
}