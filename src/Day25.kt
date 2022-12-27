fun main() {
    val charToDigit = mapOf('=' to -2, '-' to -1, '0' to 0, '1' to 1, '2' to 2)
    val digitToChar = charToDigit.map { (char, digit) -> digit.toLong() to char }.toMap()

    fun toDecimal(snafu: String) = snafu.toCharArray().reversed().mapIndexed { index, char -> charToDigit[char]!! * 5.pow(index) }.sum()

    fun toSnafu(decimal: Long): String {
        var value = decimal
        var result = ""

        while (value > 0) {
            var digit = value % 5
            if (digit > 2)
                digit -= 5
            value = (value - digit) / 5
            result += digitToChar[digit]!!
        }

        return result.reversed()
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
