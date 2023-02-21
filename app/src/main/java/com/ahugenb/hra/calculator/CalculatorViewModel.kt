package com.ahugenb.hra.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class CalculatorViewModel: ViewModel() {
    companion object {
        const val UNITS_TO_ETHANOL: Double = 10.0 / 6.0
        const val PERCENT: Double = 100.0
    }

    private val _units: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val units: StateFlow<Double> = _units

    private val _ozPureEthanol: MutableStateFlow<Double> =
        MutableStateFlow(0.0)
    val ozPureEthanol: StateFlow<Double> = _ozPureEthanol

    fun updateCalculation(abv: Double, volume: Double, drinks: Double) {
        val newOzEthanol = volume * drinks * (abv / PERCENT)
        val newUnits = newOzEthanol * UNITS_TO_ETHANOL

        _units.value = newUnits
        _ozPureEthanol.value = newOzEthanol
    }
}

