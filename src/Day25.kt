fun main() {
    val digitMap = mapOf('=' to -2, '-' to -1, '0' to 0, '1' to 1, '2' to 2)

    fun toDecimal(snafu: String) = snafu.toCharArray().reversed().mapIndexed { index, char ->  digitMap[char]!! * 5.pow(index) }.sum()

    fun toSnafu(decimal: Long): String {
        var maxExponent = 0

        while (decimal > toDecimal("2".repeat(maxExponent + 1)))
            maxExponent++

        var digits = if (decimal > toDecimal("1" + "2".repeat(maxExponent))) "2" else "1"

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

        return digits
    }

    fun part1(input: List<String>) = toSnafu(input.sumOf { toDecimal(it) })

    fun part2(input: List<String>) = "HOHOHO"

    test(
        day = 25,
        testTarget1 = "2=-1=0",
        testTarget2 = "HOHOHO",
        part1 = ::part1,
        part2 = ::part2,
    )
}
