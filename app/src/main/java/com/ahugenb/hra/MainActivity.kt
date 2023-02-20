package com.ahugenb.hra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahugenb.hra.ui.theme.HraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val menuList = mutableListOf(
                        MenuItem(0, "Unit Calculator"),
                        MenuItem(1, "Drink Tracker"),
                        MenuItem(2, "Harm Reduction Philosophy")
                    )
                    MenuList(menuList)
                }
            }
        }
    }
}

@Composable
fun MenuList(menuItems: List<MenuItem>) {
    LazyColumn {
        menuItems.forEach {
            item(key = it.id, content = { ListItem(menuItem = it) })
        }
    }
}

@Composable
fun ListItem(menuItem: MenuItem) {
    Text(modifier= Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
        text = menuItem.text,
        style = typography.h6
    )
    Divider(modifier = Modifier.height(2.dp))
}


