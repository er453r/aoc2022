import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("aoc2022/src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun assertEquals(value: Any, target: Any) {
    if (value != target)
        check(false) { "Expected $target got $value" }
}

fun test(
    day: Int,
    testTarget1: Int,
    testTarget2: Int,
    part1: (List<String>) -> Int,
    part2: (List<String>) -> Int,
) {
    // test if implementation meets criteria from the description, like:
    val dayNumber = day.toString().padStart(2, '0')
    val testInput = readInput("Day${dayNumber}_test")
    val input = readInput("Day${dayNumber}")

    println("Testing part 1 with target $testTarget1...")
    assertEquals(part1(testInput), testTarget1)
    println("Part 1")
    println(part1(input))

    println("Testing part 2 with target $testTarget2...")
    assertEquals(part2(testInput), testTarget2)
    println("Part 2")
    println(part2(input))
}
