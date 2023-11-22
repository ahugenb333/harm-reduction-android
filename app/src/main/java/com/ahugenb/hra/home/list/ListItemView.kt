package com.ahugenb.hra.home.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ListItemView(navController: NavController, menuItem: MenuItem) {
    val color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    ClickableText(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 4.dp)
        .fillMaxWidth(),
        text = AnnotatedString(menuItem.text),
        style = MaterialTheme.typography.h6.copy(color = color),
        onClick = {
            when(menuItem.id) {
                0 -> navController.navigate(NavScreen.SCREEN_CALCULATOR.title)
                1 -> navController.navigate(NavScreen.SCREEN_TRACKER.title)
                2 -> navController.navigate(NavScreen.SCREEN_GOALS.title)
            }
        }
    )
    if (menuItem.showDivider) {
        Divider(thickness = 1.dp)
    }
}