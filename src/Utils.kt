import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun readInput(name: String) = File("aoc2022/src", "$name.txt")
    .readLines()

fun <T> assertEquals(value: T, target: T) {
    if (value != target)
        check(false) { "Expected $target got $value" }
}

fun String.destructured(regex: Regex): MatchResult.Destructured = regex.matchEntire(this)
    ?.destructured
    ?: throw IllegalArgumentException("Incorrect line $this")

val intLineRegex = """-?\d+""".toRegex()

fun String.ints() = intLineRegex.findAll(this).map { it.value.toInt() }.toList()

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


class GridCell<T>(
    var value: T,
    val position: Vector2d,
)

class Grid<T>(data: List<List<T>>) {
    val data = data.mapIndexed { y, line ->
        line.mapIndexed { x, value -> GridCell(value, Vector2d(x, y)) }
    }

    val width = data.first().size
    val height = data.size

    fun get(x: Int, y: Int) = data[y][x]
    operator fun get(vector2d: Vector2d) = get(vector2d.x, vector2d.y)

    fun contains(x: Int, y: Int) = (x in 0 until width) && (y in 0 until height)

    operator fun contains(vector2d: Vector2d) = contains(vector2d.x, vector2d.y)

    fun crossNeighbours(vector2d: Vector2d) = Vector2d.DIRECTIONS.map { vector2d + it }.filter { contains(it) }.map { get(it) }

    fun path(
        start: GridCell<T>,
        end: GridCell<T>,
        heuristic: (GridCell<T>) -> Int = {
            (end.position - it.position).length()
        },
        neighbours: (GridCell<T>) -> Collection<GridCell<T>> = {
            crossNeighbours(it.position)
        },
    ) = aStar(start, end, heuristic, neighbours)
}

data class Vector2d(var x: Int = 0, var y: Int = 0) {
    companion object {
        val UP = Vector2d(0, -1)
        val DOWN = Vector2d(0, 1)
        val LEFT = Vector2d(-1, -0)
        val RIGHT = Vector2d(1, 0)
        val DIRECTIONS = arrayOf(UP, DOWN, LEFT, RIGHT)
    }

    operator fun plus(vector2d: Vector2d) = Vector2d(x + vector2d.x, y + vector2d.y)
    operator fun minus(vector2d: Vector2d) = Vector2d(x - vector2d.x, y - vector2d.y)

    fun increment(vector2d: Vector2d):Vector2d{
        this.x += vector2d.x
        this.y += vector2d.y

        return this
    }

    fun normalized() = Vector2d(if (x != 0) x / abs(x) else 0, if (y != 0) y / abs(y) else 0)

    fun negative() = Vector2d(-x, -y)

    fun length() = max(abs(x), abs(y))

    fun manhattan() =  abs(x) + abs(y)
}

data class Vector3d(var x: Int = 0, var y: Int = 0, var z:Int = 0) {
    companion object {
        val UP = Vector3d(0, -1, 0)
        val DOWN = Vector3d(0, 1 , 0)
        val LEFT = Vector3d(-1, 0, 0)
        val RIGHT = Vector3d(1, 0, 0)
        val FORWARD = Vector3d(0, 0, -1)
        val BACKWARD = Vector3d(0, 0, 1)
        val DIRECTIONS = arrayOf(UP, DOWN, LEFT, RIGHT, FORWARD, BACKWARD)
    }

    operator fun plus(vector3d: Vector3d) = Vector3d(x + vector3d.x, y + vector3d.y, z + vector3d.z)
    operator fun minus(vector3d: Vector3d) = Vector3d(x - vector3d.x, y - vector3d.y, z - vector3d.z)

    fun increment(vector3d: Vector3d):Vector3d{
        this.x += vector3d.x
        this.y += vector3d.y
        this.z += vector3d.z

        return this
    }
}

fun <Node> aStar(
    start: Node,
    end: Node,
    heuristic: (Node) -> Int,
    neighbours: (Node) -> Collection<Node>,
): List<Node> {
    val openSet = mutableSetOf(start)
    val gScores = mutableMapOf(start to 0)
    val fScores = mutableMapOf(start to heuristic(start))
    val cameFrom = mutableMapOf<Node, Node>()

    fun reconstructPath(cameFrom: Map<Node, Node>, end: Node): List<Node> {
        val path = mutableListOf(end)
        var current = end

        while (current in cameFrom) {
            current = cameFrom[current]!!
            path.add(current)
        }

        return path
    }

    while (openSet.isNotEmpty()) {
        val current = openSet.minBy { fScores[it]!! }

        if (current == end)
            return reconstructPath(cameFrom, current)

        openSet.remove(current)

        for (neighbour in neighbours(current)) {
            val neighbourScore = gScores[current]!! + 1

            if (neighbourScore < gScores.getOrDefault(neighbour, 999999)) {
                cameFrom[neighbour] = current
                gScores[neighbour] = neighbourScore
                fScores[neighbour] = neighbourScore + heuristic(neighbour)

                if (neighbour !in openSet)
                    openSet += neighbour
            }
        }
    }

    return emptyList()
}

fun List<String>.separateByBlank():List<List<String>>{
    val result = mutableListOf<List<String>>()
    var currentList = mutableListOf<String>()

    for(line in this)
        when{
            line.isBlank() && currentList.isEmpty() -> continue
            line.isBlank() -> {
                result.add(currentList)
                currentList = mutableListOf()
            }
            else -> currentList.add(line)
        }

    if(currentList.isNotEmpty())
        result.add(currentList)

    return result
}

fun Int.factorial() = (1L..this).reduce(Long::times)

fun <T> List<T>.findLongestSequence():Pair<Int, Int>{
    val sequences = mutableListOf<Pair<Int, Int>>()

    for (startPos in indices) {
        for (sequenceLength in 1..(this.size - startPos) / 2) {
            var sequencesAreEqual = true

            for (i in 0 until sequenceLength)
                if (this[startPos + i] != this[startPos + sequenceLength + i]) {
                    sequencesAreEqual = false
                    break
                }

            if (sequencesAreEqual)
                sequences += Pair(startPos, sequenceLength)
        }
    }

    return sequences.maxBy { it.second }
}
