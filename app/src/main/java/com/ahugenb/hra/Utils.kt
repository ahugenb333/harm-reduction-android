package com.ahugenb.hra

import com.ahugenb.hra.tracker.db.Day
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Utils {
    companion object {
        const val DATE_PATTERN_ID = "dd/MM/yyyy"
        private const val DATE_PATTERN_DISPLAY_LONG = "M/d/yyyy (E) "
        private const val DATE_PATTERN_DISPLAY_SHORT = "M/d/yyyy"
        private const val VALID_INPUT = "1234567890."
        /*
        * sanitized input:
        *  -is less than 10 digits in length
        *  -may not contain more than one decimal
        *  -may only contain digits from validInput
        */
        fun String.isSanitized(): Boolean =
            (this.isEmpty() || this.length < 10 && this.count { ch -> ch == '.' } < 2
                    && this.all { ch -> VALID_INPUT.contains(ch) })

        //money may only have 2 decimal places
        fun String.isSanitizedDollars(): Boolean =
            this.isSanitized() && countAfterDecimal() < 3

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
        fun Double.isValidPercent(): Boolean = this in 0.0..100.0

        fun Double.isValidDrinks(): Boolean = this in 0.0..1000.0

        fun Double.isValidVolume(): Boolean = this in 0.0..100000.0

        //Dr. Evil Voice...
        fun Double.isValidDollars(): Boolean = this in 0.0..1000000.0

        fun String.smartToDouble(): Double =
            when (this.isEmpty() || this == ".") {
                true -> 0.0
                else -> this.toDouble()
            }

        fun String.idToDateTime(): DateTime =
            DateTime.parse(this, DateTimeFormat.forPattern(DATE_PATTERN_ID))

        fun DateTime.toDisplayLong(): String = this.toString(DATE_PATTERN_DISPLAY_LONG)

        fun DateTime.toDisplayShort(): String = this.toString(DATE_PATTERN_DISPLAY_SHORT)

        fun DateTime.toId(): String = this.toString(DATE_PATTERN_ID)

        fun todaysId(): String = DateTime.now().toId()

        fun Day.isToday(): Boolean = this.id == todaysId()

        fun List<Day>.filterToday(): List<Day> = this.filter { it.isToday() }

        fun List<Day>.filterDay(day: Day) = this.filter { day.id == it.id }

        fun List<Day>.getClosestMonday(day: Day): Day {
            return this.find {
                it.id.idToDateTime().weekOfWeekyear == day.id.idToDateTime().weekOfWeekyear
                        && it.id.idToDateTime().dayOfWeek == 1
            }!!
        }

        fun Day.prettyPrintLong(): String = this.id.idToDateTime().toDisplayLong()

        fun Day.prettyPrintShort(): String = this.id.idToDateTime().toDisplayShort()
    }
}