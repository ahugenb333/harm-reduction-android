package com.ahugenb.hra.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ahugenb.hra.MenuItem

@Composable
fun MenuList(navController: NavController, menuItems: List<MenuItem>) {
    LazyColumn {
        menuItems.forEach {
            item(key = it.id, content = {
                ListItem(navController = navController, menuItem = it)
            })
        }
    }
}