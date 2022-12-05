fun main() {
    val inputLineRegex = """\d""".toRegex()

    fun lineToSets(line: String): Pair<Set<Int>, Set<Int>> {
        val (start1, end1, start2, end2) = inputLineRegex.findAll(line).map { it.value.toInt() }.toList()

        return Pair(IntRange(start1, end1).toSet(), IntRange(start2, end2).toSet())
    }

    fun part1(input: List<String>): Int = input
        .map(::lineToSets)
        .count { (a, b) -> a.containsAll(b) || b.containsAll(a) }

    fun part2(input: List<String>): Int = input
        .map(::lineToSets)
        .count { (a, b) -> a.intersect(b).isNotEmpty() }

    test(
        day = 4,
        testTarget1 = 2,
        testTarget2 = 4,
        part1 = ::part1,
        part2 = ::part2,
    )
}
