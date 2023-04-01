package com.ahugenb.hra

import com.ahugenb.hra.tracker.db.Day
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Utils {
    companion object {
        const val DATE_PATTERN_ID = "dd/MM/yyyy"
        private const val DATE_PATTERN_DISPLAY_LONG = "M/d/yyyy (E) "
        private const val DATE_PATTERN_DISPLAY_SHORT = "M/d/yyyy"
        private const val VALID_INPUT_NUMBER = "1234567890"
        private const val VALID_INPUT_DECIMAL = VALID_INPUT_NUMBER.plus(".")

        /*
        * sanitized decimals:
        *  -is less than 10 digits in length
        *  -may not contain more than one decimal
        *  -may only contain digits from validInput
        */
        fun String.isSanitizedDecimal(): Boolean =
            (this.isEmpty() || this.length < 10 && this.count { ch -> ch == '.' } < 2
                    && this.all { ch -> VALID_INPUT_DECIMAL.contains(ch) })

        //money may only have 2 decimal places
        private fun String.isSanitizedDollars(): Boolean =
            this.isSanitizedDecimal() && countAfterDecimal() < 3

        fun String.isSanitizedNumber(): Boolean =
            (this.isEmpty() || this.length < 10 && this.none { ch -> ch == '.' }
                    && this.all { ch -> VALID_INPUT_NUMBER.contains(ch) })

        private fun String.countAfterDecimal(): Int {
            var startCount = false
            var count = 0
            this.forEach {
                if (startCount) {
                    count++
                }
                if (it == '.') {
                    startCount = true
                }
            }
            return count
        }

        private fun Int.isValidCravings(): Boolean = this in 0..100

        private fun Double.isValidPercent(): Boolean = this in 0.0..100.0

        private fun Double.isValidDrinks(): Boolean = this in 0.0..1000.0

        private fun Double.isValidVolume(): Boolean = this in 0.0..100000.0

        private fun Double.isValidDollars(): Boolean = this in 0.0..1000000.0

        fun Double.rounded(): Double = String.format("%.3f", this).toDouble()

        fun String.smartToDouble(): Double =
            when (this.isEmpty() || this == ".") {
                true -> 0.0
                else -> this.toDouble()
            }

        fun String.smartToInt(): Int =
            when (this.isEmpty() || this == "") {
                true -> 0
                else -> this.toInt()
            }

        fun String.idToDateTime(): DateTime =
            DateTime.parse(this, DateTimeFormat.forPattern(DATE_PATTERN_ID))

        private fun DateTime.toDisplayLong(): String = this.toString(DATE_PATTERN_DISPLAY_LONG)

        private fun DateTime.toDisplayShort(): String = this.toString(DATE_PATTERN_DISPLAY_SHORT)

        fun DateTime.toId(): String = this.toString(DATE_PATTERN_ID)

        private fun todaysId(): String = DateTime.now().toId()

        fun Day.isToday(): Boolean = this.id == todaysId()

        fun List<Day>.filterToday(): List<Day> = this.filter { it.isToday() }

        fun List<Day>.getDrinksTotal(): Double = this.sumOf { it.drinks }

        fun List<Day>.getMoneySpentTotal(): Double = this.sumOf { it.moneySpent }

        fun List<Day>.getPlannedTotal(): Double = this.sumOf { it.planned }

        fun List<Day>.getCravingsTotal(): Int = this.sumOf { it.cravings }

        fun String.acceptPercentText(): Boolean =
            this.isSanitizedDecimal() && this.smartToDouble().isValidPercent()

        fun String.acceptDrinksText(): Boolean =
            this.isSanitizedDecimal() && this.smartToDouble().isValidDrinks()

        fun String.acceptDollarsText(): Boolean =
            this.isSanitizedDollars() && this.smartToDouble().isValidDollars()

        fun String.acceptVolumeText(): Boolean =
            this.isSanitizedDecimal() && this.smartToDouble().isValidVolume()

        fun String.acceptCravingsText(): Boolean =
            this.isSanitizedNumber() && this.smartToInt().isValidCravings()

        fun Day.prettyPrintLong(): String = this.id.idToDateTime().toDisplayLong()

        fun Day.prettyPrintShort(): String = this.id.idToDateTime().toDisplayShort()
    }
}