package com.ahugenb.hra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahugenb.hra.calculator.CalculatorView
import com.ahugenb.hra.calculator.CalculatorViewModel
import com.ahugenb.hra.list.MenuList
import com.ahugenb.hra.ui.theme.HraTheme

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HraTheme {
                val navController = rememberNavController()
                val menuList = mutableListOf(
                    MenuItem(0, "Unit Calculator (USA)"),
                    MenuItem(1, "Drink Tracker / Planner"),
                    MenuItem(2, "Harm Reduction Philosophy")
                )

                NavHost(navController, startDestination = NavScreen.SCREEN_HOME.title) {
                    composable(NavScreen.SCREEN_HOME.title) {
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