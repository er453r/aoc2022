fun main() {
    fun ropeTailTravel(input: List<String>, ropeLength: Int): Int {
        val knots = Array(ropeLength) { Vector2d(0, 0) }
        val visited = mutableSetOf(knots.last())

        for (command in input) {
            val (direction, steps) = command.split(" ")

            repeat(steps.toInt()) {
                knots[0] += when (direction) {
                    "U" -> Vector2d.UP
                    "D" -> Vector2d.DOWN
                    "L" -> Vector2d.LEFT
                    "R" -> Vector2d.RIGHT
                    else -> throw Exception("Unknown command $direction")
                }

                for (n in 1 until knots.size) {
                    val diff = knots[n - 1] - knots[n]

                    if (diff.length() > 1) { // we need to move
                        knots[n] += diff.normalized()

                        if (n == knots.size - 1)
                            visited.add(knots[n])
                    }
                }
            }
        }

        return visited.size
    }

    fun part1(input: List<String>) = ropeTailTravel(input, 2)

    fun part2(input: List<String>) = ropeTailTravel(input, 10)

    test(
        day = 9,
        testTarget1 = 13,
        testTarget2 = 1,
        part1 = ::part1,
        part2 = ::part2,
    )
}
