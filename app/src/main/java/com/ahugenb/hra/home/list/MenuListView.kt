package com.ahugenb.hra.home.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MenuListView(navController: NavController, menuItems: List<MenuItem>) {
    LazyColumn {
        menuItems.forEach {
            item(key = it.id, content = {
                ListItemView(navController = navController, menuItem = it)
            })
        }
    }
}