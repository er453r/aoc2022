fun main() {
    val inputLineRegex = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()

    fun lineToSets(line: String): Pair<Set<Int>, Set<Int>> {
        val (start1, end1, start2, end2) = line.destructured(inputLineRegex)

        return Pair(IntRange(start1.toInt(), end1.toInt()).toSet(), IntRange(start2.toInt(), end2.toInt()).toSet())
    }

    fun part1(input: List<String>): Int = input
        .map(::lineToSets)
        .map { (a, b) -> a.containsAll(b) || b.containsAll(a) }
        .sumTrue()

    fun part2(input: List<String>): Int = input
        .map(::lineToSets)
        .map { (a, b) -> a.intersect(b).isNotEmpty() }
        .sumTrue()

    test(
        day = 4,
        testTarget1 = 2,
        testTarget2 = 4,
        part1 = ::part1,
        part2 = ::part2,
    )
}
