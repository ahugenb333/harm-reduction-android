package com.ahugenb.hra.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class CalculatorViewModel: ViewModel() {
    companion object {
        const val OZ_ETHANOL_TO_UNITS: Double = 10.0 / 6.0
        const val ML_ETHANOL_TO_UNITS: Double = 1.0 / 17.7
        const val PERCENT: Double = 100.0
    }

    private val _units: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val units: StateFlow<Double> = _units

    private val _pureEthanol: MutableStateFlow<Double> =
        MutableStateFlow(0.0)
    val pureEthanol: StateFlow<Double> = _pureEthanol

    fun updateCalculation(abv: Double, volume: Double, drinks: Double, mlChecked: Boolean) {
        val newEthanol = volume * drinks * (abv / PERCENT)
        val newUnits =
            if (mlChecked)
                newEthanol * ML_ETHANOL_TO_UNITS
            else
                newEthanol * OZ_ETHANOL_TO_UNITS

        _units.value = newUnits
        _pureEthanol.value = newEthanol
    }

    fun clear() {
        _units.value = 0.0
        _pureEthanol.value = 0.0
    }
}

