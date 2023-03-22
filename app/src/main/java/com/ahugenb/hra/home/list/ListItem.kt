package com.ahugenb.hra.home.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.MenuItem
import com.ahugenb.hra.NavScreen

@Composable
fun ListItem(navController: NavController, menuItem: MenuItem) {
    ClickableText(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 4.dp)
        .fillMaxWidth(),
        text = AnnotatedString(menuItem.text),
        style = MaterialTheme.typography.h6,
        onClick = {
            when(menuItem.id) {
                0 -> navController.navigate(NavScreen.SCREEN_CALCULATOR.title)
                1 -> navController.navigate(NavScreen.SCREEN_TRACKER.title)
            }
        }
    )
    Divider(modifier = Modifier.height(2.dp))
}