fun main() {
    val lowerScoreOffset = 'a'.code - 1
    val upperScoreOffset = 'A'.code - 1 - 26

    fun Char.priority(): Int = this.code - (if (this.code > lowerScoreOffset) lowerScoreOffset else upperScoreOffset)

    fun part1(input: List<String>): Int = input.sumOf { items ->
        items.chunked(items.length / 2)
            .map { it.toSet() }
            .reduce { a, b -> a.intersect(b) }
            .first()
            .priority()
    }

    fun part2(input: List<String>): Int = input.chunked(3).sumOf { group ->
        group.map { it.toSet() }
            .reduce { a, b -> a.intersect(b) }
            .first()
            .priority()
    }

    test(
        day = 3,
        testTarget1 = 157,
        testTarget2 = 70,
        part1 = ::part1,
        part2 = ::part2,
    )
}
