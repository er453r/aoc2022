import java.io.File

fun readInput(name: String) = File("aoc2022/src", "$name.txt")
    .readLines()

fun <T> assertEquals(value: T, target: T) {
    if (value != target)
        check(false) { "Expected $target got $value" }
}

fun String.destructured(regex: Regex): MatchResult.Destructured = regex.matchEntire(this)
    ?.destructured
    ?: throw IllegalArgumentException("Incorrect line $this")

fun <T> test(
    day: Int,
    testTarget1: T,
    testTarget2: T,
    part1: (List<String>) -> T,
    part2: (List<String>) -> T,
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

class IntGrid(input: List<String>){
    val grid = input.map { it.toCharArray().map { char -> char.digitToInt() } }
    val width = grid.first().size
    val height = grid.size

    fun get(x:Int, y:Int) = grid[y][x]
    operator fun get(vector2d: Vector2d) = get(vector2d.x, vector2d.y)

    fun contains(x:Int, y:Int) = x >= 0 && x < width && y >= 0 && y < height

    operator fun contains(vector2d: Vector2d) = contains(vector2d.x, vector2d.y)
}

data class Vector2d(var x: Int = 0, var y: Int = 0){
    operator fun plus(vector2d: Vector2d) = Vector2d(x + vector2d.x, y + vector2d.y)

    fun increment(dx: Int, dy:Int){
        x += dx
        y += dy
    }
}

enum class Direction2d(val vector2d: Vector2d) {
    RIGHT(Vector2d(1, 0)),
    DOWN(Vector2d(0, 1)),
    UP(Vector2d(0, -1)),
    LEFT(Vector2d(-1, 0)),
}
