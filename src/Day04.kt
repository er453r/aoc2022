fun main() {
    val inputLineRegex = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()

    fun part1(input: List<String>): Int {
        var sum = 0

        for(line in input){
            val (start1, end1, start2, end2) = inputLineRegex.matchEntire(line)
                ?.destructured
                ?: throw IllegalArgumentException("Incorrect input line $line")

            val first = IntRange(start1.toInt(), end1.toInt()).toSet()
            val second = IntRange(start2.toInt(), end2.toInt()).toSet()

            if(first.containsAll(second) || second.containsAll(first))
                sum += 1
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for(line in input){
            val (start1, end1, start2, end2) = inputLineRegex.matchEntire(line)
                ?.destructured
                ?: throw IllegalArgumentException("Incorrect input line $line")

            val first = IntRange(start1.toInt(), end1.toInt()).toSet()
            val second = IntRange(start2.toInt(), end2.toInt()).toSet()

            if(first.intersect(second).isNotEmpty())
                sum += 1
        }

        return sum
    }

    test(
        day = 4,
        testTarget1 = 2,
        testTarget2 = 4,
        part1 = ::part1,
        part2 = ::part2,
    )
}
