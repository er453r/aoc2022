fun main() {
    fun part1(input: List<String>): Int {
        val grid = IntGrid(input)

        val outside = 2 * grid.width + 2 * (grid.height - 2)
        var otherVisible = 0

        val directions = Direction2d.values()

        for (x in 1 until grid.width - 1)
            for (y in 1 until grid.height - 1) {
                var visible = false
                val value = grid.get(x, y)
                val position = Vector2d(x, y)

                dirs@ for (direction in directions) {
                    var neighbour = position + direction.vector2d

                    while (neighbour in grid) {
                        if (grid[neighbour] >= value) // not visible - check other directions
                            continue@dirs

                        neighbour += direction.vector2d

                        if (!(neighbour in grid)) { // we reached the end - it is visible!
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
        val grid = input.map { it.toCharArray().map { char -> char.digitToInt() } }

        val width = grid.first().size
        val height = grid.size

        var bestScore = 0

        for (x in 1 until width - 1)
            for (y in 1 until height - 1) {
                val value = grid[y][x]

                var rightScore = 1
                var leftScore = 1
                var upScore = 1
                var downScore = 1

                for (nx in x + 1 until width) {
                    if (grid[y][nx] >= value)
                        break

                    if (nx != width - 1)
                        rightScore += 1
                }

                for (nx in x - 1 downTo 0) {
                    if (grid[y][nx] >= value)
                        break

                    if (nx != 0)
                        leftScore += 1
                }

                for (ny in y + 1 until height) {
                    if (grid[ny][x] >= value)
                        break

                    if (ny != height - 1)
                        upScore += 1
                }

                for (ny in y - 1 downTo 0) {
                    if (grid[ny][x] >= value)
                        break

                    if (ny != 0)
                        downScore += 1
                }

                val score = upScore * downScore * leftScore * rightScore

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
