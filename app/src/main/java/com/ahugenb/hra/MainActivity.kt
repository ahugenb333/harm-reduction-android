package com.ahugenb.hra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahugenb.hra.calculator.CalculatorView
import com.ahugenb.hra.calculator.CalculatorViewModel
import com.ahugenb.hra.home.list.MenuItem
import com.ahugenb.hra.home.list.MenuList
import com.ahugenb.hra.home.list.NavScreen
import com.ahugenb.hra.home.quickaction.QuickActionView
import com.ahugenb.hra.tracker.TrackerView
import com.ahugenb.hra.tracker.TrackerViewModel
import com.ahugenb.hra.tracker.TrackerViewModelFactory
import com.ahugenb.hra.ui.theme.HraTheme

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private val trackerViewModel: TrackerViewModel by viewModels {
        TrackerViewModelFactory((application as HraApplication).dayRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HraTheme {
                val navController = rememberNavController()
                val menuList = mutableListOf(
                    MenuItem(0, "Unit Calculator (USA)"),
                    MenuItem(1, "Drink Tracker / Planner"),
                    MenuItem(2, "Harm Reduction Philosophy"),
                    MenuItem(3, "Quick Actions", showDivider = false)
                )

                //TODO use Hilt to avoid passing dependencies through

                NavHost(navController, startDestination = NavScreen.SCREEN_LIST.title) {
                    composable(NavScreen.SCREEN_LIST.title) {
                        Column {
                            MenuList(navController, menuList)
                            QuickActionView(trackerViewModel)
                        }
                    }

                    composable(NavScreen.SCREEN_CALCULATOR.title) {
                        CalculatorView(navController, calculatorViewModel)
                    }

                    composable(NavScreen.SCREEN_TRACKER.title) {
                        TrackerView(trackerViewModel)
                    }
                }
            }
        }
    }
}