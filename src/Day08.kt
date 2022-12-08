fun main() {
    fun part1(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().asList().map(Char::digitToInt) })

        val outside = 2 * grid.width + 2 * (grid.height - 2)
        var otherVisible = 0

        for (x in 1 until grid.width - 1)
            for (y in 1 until grid.height - 1) {
                var visible = false
                val cell = grid.get(x, y)

                dirs@ for (direction in Vector2d.DIRECTIONS) {
                    var neighbour = cell.position + direction

                    while (neighbour in grid) {
                        if (grid[neighbour].value >= cell.value) // not visible - check other directions
                            continue@dirs

                        neighbour += direction

                        if (neighbour !in grid) { // we reached the end - it is visible!
                            visible = true
                            break@dirs
                        }
                    }
                }

                if (visible)
                    otherVisible += 1
            }

        return outside + otherVisible
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().asList().map(Char::digitToInt) })

        var bestScore = 0

        for (x in 1 until grid.width - 1)
            for (y in 1 until grid.height - 1) {
                val cell = grid.get(x, y)

                val score = Vector2d.DIRECTIONS.map { direction ->
                    var dirScore = 0
                    var neighbour = cell.position + direction

                    while (neighbour in grid) {
                        dirScore += 1

                        if (grid[neighbour].value >= cell.value)
                            break

                        neighbour += direction
                    }

                    dirScore
                }.reduce(Int::times)

                if (score > bestScore)
                    bestScore = score
            }

        return bestScore
    }

    test(
        day = 8,
        testTarget1 = 21,
        testTarget2 = 8,
        part1 = ::part1,
        part2 = ::part2,
    )
}
