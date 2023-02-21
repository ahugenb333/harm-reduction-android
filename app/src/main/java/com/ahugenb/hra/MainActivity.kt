package com.ahugenb.hra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahugenb.hra.calculator.CalculatorViewModel
import com.ahugenb.hra.ui.theme.HraTheme

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HraTheme {
                val navController = rememberNavController()
                val menuList = mutableListOf(
                    MenuItem(0, "Unit Calculator"),
                    MenuItem(1, "Drink Tracker"),
                    MenuItem(2, "Harm Reduction Philosophy")
                )

                NavHost(navController, startDestination = NavScreen.SCREEN_LIST.title) {
                    composable(NavScreen.SCREEN_LIST.title) {
                        MenuList(navController, menuList)
                    }

                    composable(NavScreen.SCREEN_CALCULATOR.title) {
                        CalculatorView(navController, calculatorViewModel)
                    }
                }
            }
        }
    }
}

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

@Composable
fun ListItem(navController: NavController, menuItem: MenuItem) {
    ClickableText(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 4.dp)
        .fillMaxWidth(),
        text = AnnotatedString(menuItem.text),
        style = typography.h6,
        onClick = { handleClick(navController, menuItem.id) }
    )
    Divider(modifier = Modifier.height(2.dp))
}

private fun handleClick(navController: NavController, id: Int) {
    when (id) {
        0 -> navController.navigate("screenCalculator")
    }
}

@Composable
fun CalculatorView(navController: NavController, viewModel: CalculatorViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val volume = remember { mutableStateOf(0.0) }

        OutlinedTextField(
            value = volume.value.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                volume.value = it.toDouble()
                viewModel.updateVolume(volume.value)
            },
            label = { Text(text = "Oz") }
        )
    }
}

enum class NavScreen(val title: String) {
    SCREEN_LIST("screenList"),
    SCREEN_CALCULATOR("screenCalculator"),
    SCREEN_PHILOSOPHY("screenPhilosophy")
}