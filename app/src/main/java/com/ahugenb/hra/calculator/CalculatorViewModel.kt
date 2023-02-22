package com.ahugenb.hra.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel: ViewModel() {
    companion object {
        const val OZ_ETHANOL_TO_UNITS: Double = 10.0 / 6.0
        const val ML_ETHANOL_TO_UNITS: Double = 1.0 / 17.7
        const val PERCENT: Double = 100.0
    }

    private val _calculatorState: MutableStateFlow<CalculatorState> =
        MutableStateFlow(CalculatorState(units = 0.0, ethanol = 0.0))
    val calculatorState = _calculatorState.asStateFlow()

    fun updateCalculation(abv: Double, volume: Double, drinks: Double, mlChecked: Boolean) {
        val newEthanol = volume * drinks * (abv / PERCENT)
        val newUnits =
            if (mlChecked)
                newEthanol * ML_ETHANOL_TO_UNITS
            else
                newEthanol * OZ_ETHANOL_TO_UNITS

        _calculatorState.value = CalculatorState(units = newUnits, ethanol = newEthanol)
    }

    fun clear() {
        _calculatorState.value =  CalculatorState(0.0, 0.0)
    }
}

