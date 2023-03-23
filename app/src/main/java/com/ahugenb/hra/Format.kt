package com.ahugenb.hra

class Format {
    companion object {
        const val DATE_PATTERN = "dd/MM/yyyy"
        const val DATE_PATTERN_TZ = "dd/MM/yyyy HH:mm"
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
    }
}