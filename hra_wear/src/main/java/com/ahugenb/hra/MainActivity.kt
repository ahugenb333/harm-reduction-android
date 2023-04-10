/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.ahugenb.hra

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.ahugenb.hra.theme.HraWearTheme
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), OnDataChangedListener {
    private val wearViewModel: WearViewModel by viewModels {
        WearViewModelFactory((application as HraWearApplication).wearRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(wearViewModel)
        }
    }

    companion object {
        const val MESSAGE_PATH = "/message_path"
        const val DATA = "data"
    }

    public override fun onResume() {
        Wearable.getDataClient(this).addListener(this)
        super.onResume()
    }

    override fun onPause() {
        Wearable.getDataClient(this).removeListener(this)
        super.onPause()
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        Log.d("onDataChanged", dataEventBuffer.toString())
        for (event: DataEvent in dataEventBuffer) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event?.dataItem?.uri?.path
                if (path == MESSAGE_PATH) {
                    val item = DataMapItem.fromDataItem(event.dataItem)
                    var data = item.dataMap.getString(DATA) ?: break
                    Log.d(DATA, data)
                    data = data.substring(5)  //remove timestamp
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                }
            }
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