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

fun testDay(
    day: Int,
    testTarget: Int,
    part1: (List<String>) -> Int,
    part2: (List<String>) -> Int,
) {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day.toString().padStart(2, '0')}_test")
    assertEquals(part1(testInput), testTarget)

    val input = readInput("Day${day.toString().padStart(2, '0')}")
    println(part1(input))
    println(part2(input))
}
