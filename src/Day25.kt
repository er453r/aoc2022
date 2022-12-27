import java.math.BigInteger

fun main() {
    fun pow(n: Long, exp: Int): Long {
        return BigInteger.valueOf(n).pow(exp).toLong()
    }

    val digitMap = mapOf(-2 to "=", -1 to "-", 0 to "0", 1 to "1", 2 to "2")

    fun toDecimal(snafu: String) = snafu.toCharArray().reversed().mapIndexed { index, char ->
        val multiplier = when (char) {
            '-' -> -1
            '=' -> -2
            else -> char.digitToInt()
        }

        multiplier * pow(5, index)
    }.sum()

    fun toSnafu(decimal: Long): String {
        var maxExponent = 0

//        println("\n\nConverting $decimal")

        while (decimal > toDecimal("2".repeat(maxExponent + 1)))
            maxExponent++

        var digits = if (decimal > toDecimal("1" + "2".repeat(maxExponent))) "2" else "1"

//        println("maxExponent $maxExponent")
//        println("firstDigit $digits")

        digitLoop@while (digits.length < maxExponent + 1) {
            if(decimal > toDecimal(digits + "1" + "2".repeat(maxExponent - digits.length))) {
                digits += "2"
                continue@digitLoop
            }

            if(decimal > toDecimal(digits + "0" + "2".repeat(maxExponent - digits.length))) {
                digits += "1"
                continue@digitLoop
            }

            if(decimal < toDecimal(digits + "-" + "=".repeat(maxExponent - digits.length))) {
                digits += "="
                continue@digitLoop
            }

            if(decimal < toDecimal(digits + "0" + "=".repeat(maxExponent - digits.length))) {
                digits += "-"
                continue@digitLoop
            }

            digits += "0"
        }

//        println("$decimal = $digits")

        return digits
    }

    fun part1(input: List<String>): String {
        val sum = input.map { toDecimal(it) }.sum()

        println(sum)

//        toSnafu(14)
//        toSnafu(12)
//        toSnafu(13)
//        toSnafu(62)

        return toSnafu(sum)
    }

    fun part2(input: List<String>): String {
        return "DERP"
    }

    test(
        day = 25,
        testTarget1 = "2=-1=0",
        testTarget2 = "DERP",
        part1 = ::part1,
        part2 = ::part2,
    )
}
