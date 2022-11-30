fun main() {
    val day = 1
    val testTarget = 3

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day.toString().padStart(2, '0')}_test")
    assertEquals(part1(testInput), testTarget)

    val input = readInput("Day${day.toString().padStart(2, '0')}")
    println(part1(input))
    println(part2(input))
}
