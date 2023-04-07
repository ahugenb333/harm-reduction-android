package com.ahugenb.hra

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahugenb.hra.Utils.Companion.serializable
import com.ahugenb.hra.calculator.CalculatorView
import com.ahugenb.hra.calculator.CalculatorViewModel
import com.ahugenb.hra.home.list.MenuItem
import com.ahugenb.hra.home.list.MenuListView
import com.ahugenb.hra.home.list.NavScreen
import com.ahugenb.hra.home.quickaction.QuickActionView
import com.ahugenb.hra.sync.SyncViewModel
import com.ahugenb.hra.sync.SyncViewModelFactory
import com.ahugenb.hra.sync.SyncWearableData
import com.ahugenb.hra.sync.TestIdView
import com.ahugenb.hra.tracker.TrackerView
import com.ahugenb.hra.tracker.TrackerViewModel
import com.ahugenb.hra.tracker.TrackerViewModelFactory
import com.ahugenb.hra.ui.theme.HraTheme

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private val trackerViewModel: TrackerViewModel by viewModels {
        TrackerViewModelFactory((application as HraApplication).dayRepository)
    }
    private val syncViewModel: SyncViewModel by viewModels {
        SyncViewModelFactory((application as HraApplication).syncRepository)
    }

    companion object {
        const val MESSAGE_HALF_DRINK = "half_drink"
        const val MESSAGE_DRINK = "drink"
        const val MESSAGE_CRAVING = "craving"
        const val MESSAGE_MONEY = "money"
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val payload = intent?.serializable<SyncWearableData>("payload")
            Log.d("onReceive", payload.toString())
            when (payload?.message) {
                MESSAGE_HALF_DRINK -> trackerViewModel.addDrinksToday(0.5)
                MESSAGE_DRINK -> trackerViewModel.addDrinksToday(1.0)
                MESSAGE_CRAVING -> trackerViewModel.addCravingsToday(1)
                MESSAGE_MONEY -> {
                    payload.moneySpent?.let { trackerViewModel.addMoneySpentToday(it) }
                }
            }
        }
        //todo send message back to watch for toast display
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
            IntentFilter(Intent.ACTION_SEND))
        setContent {
            HraTheme {
                val navController = rememberNavController()
                val menuList = mutableListOf(
                    MenuItem(0, "Unit Calculator (USA)"),
                    MenuItem(1, "Drink Tracker / Planner"),
                    MenuItem(3, "Quick Actions", showDivider = false)
                )

                //TODO use Hilt to avoid passing dependencies through

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                ) {
                    NavHost(navController, startDestination = NavScreen.SCREEN_LIST.title) {
                        composable(NavScreen.SCREEN_LIST.title) {
                            Column {
                                MenuListView(navController, menuList)
                                QuickActionView(trackerViewModel)
                                TestIdView(syncViewModel)
                            }
                        }

                        composable(NavScreen.SCREEN_CALCULATOR.title) {
                            CalculatorView(calculatorViewModel, trackerViewModel, navController)
                        }

                        composable(NavScreen.SCREEN_TRACKER.title) {
                            TrackerView(trackerViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}