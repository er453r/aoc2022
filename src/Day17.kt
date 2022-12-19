import kotlin.math.min

fun main() {
    val blockShapes = """####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##"""

    class Block(val points: List<Vector2d>) {
        fun move(vector2d: Vector2d) = points.forEach { it.increment(vector2d) }

        fun collides(other: Set<Vector2d>) = points.any { it in other }

        fun copy(): Block = Block(points.map { it.copy() })
    }

    val blocks = blockShapes.lines().separateByBlank().asSequence()
        .map { it.map { line -> line.toCharArray().toList() } }
        .map { Grid(it) }
        .map { grid -> grid.data.flatten().filter { cell -> cell.value == '#' }.map { it.position } }
        .map {
            val dy = it.maxOf { point -> point.y } + 1

            it.map { point -> point.increment(Vector2d(0, -dy)) }
        }
        .map { Block(it) }
        .toList()

    fun tetris(input: List<String>, blocksToFall: Int): Pair<Long, List<Int>> {
        val moves = input.first().toCharArray().map { if (it == '<') Vector2d.LEFT else Vector2d.RIGHT }
        val rightWall = 8
        val settled = mutableSetOf<Vector2d>()
        val increments = mutableListOf<Int>()

        var currentTop = 0L
        var currentBlockIndex = 0
        var currentMoveIndex = 0
        var rocksFallen = blocksToFall

        while (rocksFallen-- != 0) {
            val block = blocks[currentBlockIndex++ % blocks.size].copy()

            block.move(Vector2d(3, currentTop.toInt() - 3)) // move to start position

            while (true) {
                val wind = moves[currentMoveIndex++ % moves.size] // first wind movement

                block.move(wind)

                if (block.collides(settled) ||
                    block.points.minOf { it.x } == 0 ||
                    block.points.maxOf { it.x } == rightWall
                )
                    block.move(wind.negative()) // move back

                block.move(Vector2d.DOWN)

                if (block.collides(settled) || block.points.minOf { it.y } == 0) { // block settles
                    block.move(Vector2d.UP) // move back
                    settled.addAll(block.points)
                    val newCurrentTop = min(currentTop, block.points.minOf { it.y }.toLong())
                    increments.add((currentTop - newCurrentTop).toInt())
                    currentTop = newCurrentTop

                    break
                }
            }
        }

        return Pair(-currentTop, increments)
    }

    fun part1(input: List<String>) = tetris(input, 2022).first

    fun part2(input: List<String>): Long {
        val (_, increments) = tetris(input, 8000)
        val bestSequence = increments.findLongestSequence()
        val iters = 1000000000000L
        val sequenceHeight = increments.subList(bestSequence.first, bestSequence.first + bestSequence.second).sum()

        var height = increments.subList(0, bestSequence.first).sum().toLong()
        var itersLeft = iters - bestSequence.first
        val sequencesLeft = itersLeft / bestSequence.second

        height += sequencesLeft * sequenceHeight
        itersLeft -= sequencesLeft * bestSequence.second
        height += increments.subList(bestSequence.first, bestSequence.first + itersLeft.toInt()).sum()

        return height
    }

    test(
        day = 17,
        testTarget1 = 3068L,
        testTarget2 = 1514285714288L,
        part1 = ::part1,
        part2 = ::part2,
    )
}
