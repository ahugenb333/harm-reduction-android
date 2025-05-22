package com.ahugenb.hra.calculator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CalculatorViewModelTest {

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.calculatorState.first()
        assertEquals(0.0, state.units, 0.001)
        assertEquals(0.0, state.ethanol, 0.001)
    }

    @Test
    fun `onUpdateCalculation calculates correctly for oz`() = runTest {
        // Given
        val abv = 5.0  // 5%
        val volume = 12.0 // 12 oz
        val drinks = 1.0 // 1 drink
        val mlChecked = false

        // Expected values
        // ethanol = 12.0 * 1.0 * (5.0 / 100.0) = 12.0 * 0.05 = 0.6 oz of ethanol
        // units = 0.6 * (10.0 / 6.0) = 0.6 * 1.666... = 1.0
        val expectedEthanol = 0.6
        val expectedUnits = 1.0

        // When
        viewModel.onUpdateCalculation(abv, volume, drinks, mlChecked)
        val state = viewModel.calculatorState.first()

        // Then
        assertEquals(expectedUnits, state.units, 0.001)
        assertEquals(expectedEthanol, state.ethanol, 0.001)
    }

    @Test
    fun `onUpdateCalculation calculates correctly for ml`() = runTest {
        // Given
        val abv = 5.0  // 5%
        val volume = 355.0 // 355 ml (approx 12 oz)
        val drinks = 1.0 // 1 drink
        val mlChecked = true

        // Expected values
        // ethanol = 355.0 * 1.0 * (5.0 / 100.0) = 355.0 * 0.05 = 17.75 ml of ethanol
        // units = 17.75 * (1.0 / 17.7) = 17.75 * 0.05649... approx 1.0028
        val expectedEthanol = 17.75
        val expectedUnits = 17.75 * CalculatorViewModel.ML_ETHANOL_TO_UNITS // Use constant for precision

        // When
        viewModel.onUpdateCalculation(abv, volume, drinks, mlChecked)
        val state = viewModel.calculatorState.first()

        // Then
        assertEquals(expectedUnits, state.units, 0.001)
        assertEquals(expectedEthanol, state.ethanol, 0.001)
    }
    
    @Test
    fun `onUpdateCalculation with multiple drinks calculates correctly for oz`() = runTest {
        // Given
        val abv = 5.0  // 5%
        val volume = 12.0 // 12 oz
        val drinks = 2.0 // 2 drinks
        val mlChecked = false

        // Expected values
        // ethanol = 12.0 * 2.0 * (5.0 / 100.0) = 24.0 * 0.05 = 1.2 oz of ethanol
        // units = 1.2 * (10.0 / 6.0) = 1.2 * 1.666... = 2.0
        val expectedEthanol = 1.2
        val expectedUnits = 2.0

        // When
        viewModel.onUpdateCalculation(abv, volume, drinks, mlChecked)
        val state = viewModel.calculatorState.first()

        // Then
        assertEquals(expectedUnits, state.units, 0.001)
        assertEquals(expectedEthanol, state.ethanol, 0.001)
    }


    @Test
    fun `onClear resets state`() = runTest {
        // Given: Update state to non-default values
        viewModel.onUpdateCalculation(5.0, 12.0, 1.0, false)
        var state = viewModel.calculatorState.first()
        assertNotEquals(0.0, state.units, 0.001) // Verify it's not default

        // When
        viewModel.onClear()
        state = viewModel.calculatorState.first()

        // Then
        assertEquals(0.0, state.units, 0.001)
        assertEquals(0.0, state.ethanol, 0.001)
    }
}
