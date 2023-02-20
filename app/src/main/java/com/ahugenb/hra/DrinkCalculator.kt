package com.ahugenb.hra

class DrinkCalculator {
    //abv = alcohol by volume in %
    fun getUnits(volume: Double, abv: Double) = volume * (abv / 100.0)

    fun getUnits(volume: Double, abv: Double, drinks: Double) = drinks * getUnits(volume, abv)

    fun getDrinks(volume: Double, abv: Double, units: Double) = units / (volume * (abv / 100))

    fun getVolume(abv: Double, drinks: Double, units: Double) = units / (drinks * (abv / 100))

    fun getAbv(volume: Double, drinks: Double, units: Double) = (units * 100) / (volume * drinks)

    fun Double.mlToOz(volumeMl: Double) = volumeMl / ML_PER_OUNCE

    fun Double.ozToMl(volumeOz: Double) = volumeOz * ML_PER_OUNCE

    companion object {
        const val ONE_UNIT = 0.6
        const val ML_PER_OUNCE = 29.5735
    }
}