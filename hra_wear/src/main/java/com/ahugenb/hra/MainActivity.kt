/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.ahugenb.hra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.ahugenb.hra.theme.HraWearTheme

class MainActivity : ComponentActivity() {
    private val wearViewModel: WearViewModel by viewModels {
        WearViewModelFactory((application as HraWearApplication).wearRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(wearViewModel)
        }
    }
}

@Composable
fun WearApp(viewModel: WearViewModel) {
    HraWearTheme {
        Surface {
            QuickActionView(viewModel)
        }
    }
}