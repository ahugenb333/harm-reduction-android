package com.ahugenb.hra.calculator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(): ViewModel() {
    companion object {
        const val OZ_ETHANOL_TO_UNITS: Double = 10.0 / 6.0
        const val ML_ETHANOL_TO_UNITS: Double = 1.0 / 17.7
        const val PERCENT: Double = 100.0
    }

    private val _calculatorState: MutableStateFlow<CalculatorState> =
        MutableStateFlow(CalculatorState())
    val calculatorState = _calculatorState.asStateFlow()

    val onUpdateCalculation: (abv: Double, volume: Double, drinks: Double, mlChecked: Boolean) -> Unit =
        { abv, volume, drinks, mlChecked ->
            val newEthanol = volume * drinks * (abv / PERCENT)
            val newUnits =
                if (mlChecked)
                    newEthanol * ML_ETHANOL_TO_UNITS
                else
                    newEthanol * OZ_ETHANOL_TO_UNITS

            _calculatorState.value = CalculatorState(units = newUnits, ethanol = newEthanol)
        }

    val onClear: () -> Unit = {
        _calculatorState.value = CalculatorState()
    }

    // Original methods can be made private or removed if logic is fully encapsulated by lambdas
    // For this case, the logic is simple enough to be directly in the lambdas.
}

