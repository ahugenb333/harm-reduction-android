package com.ahugenb.hra

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class WearViewModel(
    private val wearRepository: WearRepository
): ViewModel() {

    fun sendWholeDrink() {
        viewModelScope.launch {
            wearRepository.sendWholeDrink()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.e("Error sending whole drink", it.toString())
                }
                .collect {
                    Log.d("Whole drink sent successfully", "")
                }
        }
    }

    fun sendHalfDrink() {
        viewModelScope.launch {
            wearRepository.sendHalfDrink()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.e("Error sending whole drink", it.toString())
                }
                .collect {
                    Log.d("Half drink sent successfully", "")
                }
        }
    }

    fun sendCraving() {
        viewModelScope.launch {
            wearRepository.sendCraving()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.e("Error sending whole drink", it.toString())
                }
                .collect {
                    Log.d("Craving sent successfully", "")
                }
        }
    }

    fun sendMoneySpent(money: Double) {
        viewModelScope.launch {
            wearRepository.sendMoney(money)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.e("Error sending whole drink", it.toString())
                }
                .collect {
                    Log.d("Money sent successfully", "")
                }
        }
    }
}

class WearViewModelFactory(private val wearRepository: WearRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WearViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WearViewModel(wearRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}