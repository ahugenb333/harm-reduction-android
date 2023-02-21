package com.ahugenb.hra.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToLong

class CalculatorViewModel: ViewModel() {
    companion object {
        const val ETHANOL_TO_UNITS: Double = 10.0 / 6.0
        const val PERCENT: Double = 100.0
    }

    private val _units: MutableStateFlow<String> = MutableStateFlow("0.00 Units")
    val units: StateFlow<String> = _units

    private val _ozPureEthanol: MutableStateFlow<String> =
        MutableStateFlow("0.00 fl Oz Pure Ethanol")
    val ozPureEthanol: StateFlow<String> = _ozPureEthanol

    fun updateUnits(abv: Double, volume: Double, drinks: Double) {
        val newOzEthanol = volume * drinks * (abv / PERCENT)
        val newUnits = newOzEthanol * ETHANOL_TO_UNITS
        val unitsText: String = String.format("%.2f Units", newUnits)
        val ozEthanolText: String = String.format("%.2f fl Oz Pure Ethanol", newOzEthanol)

        _units.value = unitsText
        _ozPureEthanol.value = ozEthanolText
    }
}

