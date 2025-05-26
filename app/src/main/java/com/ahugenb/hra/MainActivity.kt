package com.ahugenb.hra

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // Added import for by viewModels()
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource // Added import
import androidx.hilt.navigation.compose.hiltViewModel // Added import for hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahugenb.hra.calculator.CalculatorView
// CalculatorViewModel will be obtained using hiltViewModel
import com.ahugenb.hra.home.list.MenuItem
import com.ahugenb.hra.home.list.MenuListView
import com.ahugenb.hra.home.list.NavScreen
import com.ahugenb.hra.home.quickaction.QuickActionView
// SyncViewModel and TrackerViewModel will be obtained using hiltViewModel
// TrackerViewModelFactory will be removed
import com.ahugenb.hra.tracker.TrackerView
import com.ahugenb.hra.ui.theme.HraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // ViewModels are now obtained using hiltViewModel() within composables,
    // OR activity-scoped using by viewModels() here if needed by activity logic (like this BroadcastReceiver).
    private val trackerViewModel: com.ahugenb.hra.tracker.TrackerViewModel by viewModels()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("onReceive", intent?.action ?: "null")
            // Call refreshToday on the activity-scoped trackerViewModel instance
            this@MainActivity.trackerViewModel.refreshToday()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver,
            IntentFilter(Intent.ACTION_SEND)
        )
        setContent {
            HraTheme {
                val navController = rememberNavController()
                val menuList = mutableListOf(
                    MenuItem(0, stringResource(id = R.string.menu_item_unit_calculator)),
                    MenuItem(1, stringResource(id = R.string.menu_item_drink_tracker)),
                    MenuItem(3, stringResource(id = R.string.menu_item_quick_actions), showDivider = false)
                )

                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(MaterialTheme.colors.surface)
                    ) {
                        NavHost(navController, startDestination = NavScreen.SCREEN_LIST.title) {
                            composable(NavScreen.SCREEN_LIST.title) {
                                Column {
                                    MenuListView(navController, menuList)
                                    val trackerViewModel: com.ahugenb.hra.tracker.TrackerViewModel = hiltViewModel()
                                    val trackerStateValueQuickActions = trackerViewModel.trackerState.collectAsState().value
                                    if (trackerStateValueQuickActions is com.ahugenb.hra.tracker.TrackerState.TrackerStateAll) {
                                        QuickActionView(
                                            todayDrinks = trackerStateValueQuickActions.today.drinks,
                                            todayCravings = trackerStateValueQuickActions.today.cravings,
                                            todayMoneySpent = trackerStateValueQuickActions.today.moneySpent,
                                            todayPlannedDrinks = trackerStateValueQuickActions.today.planned,
                                            onAddMoneySpent = { amount ->
                                                trackerViewModel.addMoneySpentToday(amount)
                                            },
                                            onUpdateDrinks = { newDrinks ->
                                                trackerViewModel.updateDrinksToday(newDrinks)
                                            },
                                            onUpdateCravings = { newCravings ->
                                                trackerViewModel.updateCravingsToday(newCravings)
                                            }
                                        )
                                    }
                                }
                            }

                            composable(NavScreen.SCREEN_CALCULATOR.title) {
                                val trackerViewModel: com.ahugenb.hra.tracker.TrackerViewModel = hiltViewModel()
                                val calculatorViewModel: com.ahugenb.hra.calculator.CalculatorViewModel = hiltViewModel()

                                val trackerStateValue = trackerViewModel.trackerState.collectAsState().value
                                val plannedDrinksToday = if (trackerStateValue is com.ahugenb.hra.tracker.TrackerState.TrackerStateAll) {
                                    trackerStateValue.today.planned
                                } else {
                                    0.0 // Default or loading state
                                }
                                val calculatorStateValue = calculatorViewModel.calculatorState.collectAsState().value

                                CalculatorView(
                                    calculatorState = calculatorStateValue,
                                    navController = navController,
                                    onUpdateCalculation = calculatorViewModel.onUpdateCalculation,
                                    onClear = calculatorViewModel.onClear,
                                    onAddCalculatedUnitsToDay = { units ->
                                        trackerViewModel.addDrinksToday(units)
                                    },
                                    plannedDrinksToday = plannedDrinksToday
                                )
                            }

                            composable(NavScreen.SCREEN_TRACKER.title) {
                                val trackerViewModel: com.ahugenb.hra.tracker.TrackerViewModel = hiltViewModel()
                                val trackerStateValue = trackerViewModel.trackerState.collectAsState().value
                                TrackerView(
                                    trackerState = trackerStateValue,
                                    navController = navController,
                                    onUpdateSelectedMonday = trackerViewModel.onUpdateSelectedMonday,
                                    onSetSelectedDay = trackerViewModel.onSetSelectedDay,
                                    onNavigateBack = trackerViewModel.onNavigateBack,
                                    getLastWeek = { trackerViewModel.getLastWeek() },
                                    onUpdateDay = { day -> trackerViewModel.updateDay(day) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}