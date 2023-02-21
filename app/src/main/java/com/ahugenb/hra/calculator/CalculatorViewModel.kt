package com.ahugenb.hra.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.calculator.UnitCalculator.Companion.updateAbv
import com.ahugenb.hra.calculator.UnitCalculator.Companion.updateDrinks
import com.ahugenb.hra.calculator.UnitCalculator.Companion.updateVolume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel: ViewModel() {

    private val _state: MutableStateFlow<CalculatorState> = MutableStateFlow(CalculatorState(1.0, 0.0, 0.0, 0.0))
    val state: Flow<CalculatorState> = _state

    fun updateDrinks(drinks: Double) {
        val updatedState = _state.value.updateDrinks(drinks)
        emitCalculatorState(updatedState)
    }

    fun updateVolume(volume: Double) {
        val updatedState = _state.value.updateVolume(volume)
        emitCalculatorState(updatedState)
    }

    fun updateAbv(abv: Double) {
        val updatedState = _state.value.updateAbv(abv)
        emitCalculatorState(updatedState)
    }

    private fun emitCalculatorState(updatedState: CalculatorState) {
        viewModelScope.launch {
            _state.emit(updatedState)
        }
    }
}

