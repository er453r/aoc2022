import java.io.File

fun readInput(name: String) = File("aoc2022/src", "$name.txt")
    .readLines()

fun assertEquals(value: Any, target: Any) {
    if (value != target)
        check(false) { "Expected $target got $value" }
}

fun Iterable<Boolean>.sumTrue(): Int {
    var sum = 0

    for (element in this)
        if (element)
            sum += 1

    return sum
}

fun String.destructured(regex: Regex): MatchResult.Destructured = regex.matchEntire(this)
    ?.destructured
    ?: throw IllegalArgumentException("Incorrect line $this")

fun test(
    day: Int,
    testTarget1: Any,
    testTarget2: Any,
    part1: (List<String>) -> Any,
    part2: (List<String>) -> Any,
) {
    // test if implementation meets criteria from the description, like:
    val dayNumber = day.toString().padStart(2, '0')
    val testInput = readInput("Day${dayNumber}_test")
    val input = readInput("Day${dayNumber}")

    println("[DAY $day]")

    println("Part 1")
    print("  test:   $testTarget1 ")
    assertEquals(part1(testInput), testTarget1)
    println("OK")
    println("  answer: ${part1(input)}")

    println("Part 2")
    print("  test:   $testTarget2 ")
    assertEquals(part2(testInput), testTarget2)
    println("OK")
    println("  answer: ${part2(input)}")
}
