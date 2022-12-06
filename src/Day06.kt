fun main() {
    fun findUniqueSequence(haystack: String, sequenceLength: Int): Int {
        return sequenceLength + haystack.windowed(sequenceLength).indexOfFirst { it.toSet().size == sequenceLength }
    }

    fun part1(input: List<String>) = findUniqueSequence(input.first(), 4)

    fun part2(input: List<String>) = findUniqueSequence(input.first(), 14)

    test(
        day = 6,
        testTarget1 = 7,
        testTarget2 = 19,
        part1 = ::part1,
        part2 = ::part2,
    )
}
