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
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahugenb.hra.calculator.CalculatorView
import com.ahugenb.hra.calculator.CalculatorViewModel
import com.ahugenb.hra.home.export.ExportButton
import com.ahugenb.hra.home.list.MenuItem
import com.ahugenb.hra.home.list.MenuListView
import com.ahugenb.hra.home.list.NavScreen
import com.ahugenb.hra.home.quickaction.QuickActionView
import com.ahugenb.hra.sync.SyncViewModel
import com.ahugenb.hra.tracker.TrackerView
import com.ahugenb.hra.tracker.TrackerViewModel
import com.ahugenb.hra.tracker.TrackerViewModelFactory
import com.ahugenb.hra.ui.theme.HraTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : ComponentActivity(), AuthClickListener {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private val trackerViewModel: TrackerViewModel by viewModels {
        TrackerViewModelFactory((application as HraApplication).dayRepository)
    }
    private val syncViewModel: SyncViewModel by viewModels()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("onReceive", intent?.action ?: "null")
            trackerViewModel.refreshToday()
        }
    }

    override fun onAuthClicked() {
        if (!GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(this),
                Utils.SHEETS_SCOPE
            )) {
            GoogleSignIn.requestPermissions(
                this,
                0,
                GoogleSignIn.getLastSignedInAccount(this),
                Utils.SHEETS_SCOPE
            )
        }
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
                    MenuItem(2, "Quick Actions", showDivider = false)
                )

                val authList = mutableListOf(
                    MenuItem(3, "Export to Google Sheets", showDivider = false),
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
                                Divider(thickness = 1.dp)
                                ExportButton(this@MainActivity)
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

interface AuthClickListener {
    fun onAuthClicked()
}