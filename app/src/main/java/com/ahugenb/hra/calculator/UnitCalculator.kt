package com.ahugenb.hra.calculator

class UnitCalculator {
    companion object {
        const val ONE_UNIT = 0.6
        const val ML_PER_OUNCE = 29.5735

        //abv = alcohol by volume in %
        private fun getUnits(volume: Double, abv: Double) = volume * (abv / 100.0)

        private fun getUnits(volume: Double, abv: Double, drinks: Double) = drinks * getUnits(volume, abv)

        fun Double.mlToOz(volumeMl: Double) = volumeMl / ML_PER_OUNCE

        fun Double.ozToMl(volumeOz: Double) = volumeOz * ML_PER_OUNCE

        private fun CalculatorState.updateUnits(): CalculatorState {
            this.units = getUnits(this.volume, this.abv, this.drinks)
            return this
        }

        fun CalculatorState.updateDrinks(drinks: Double): CalculatorState {
            this.drinks = drinks
            return updateUnits()
        }

        fun CalculatorState.updateVolume(volume: Double): CalculatorState {
            this.volume = volume
            return updateUnits()
        }

        fun CalculatorState.updateAbv(abv: Double): CalculatorState {
            this.abv = abv
            return updateUnits()
        }
    }
}