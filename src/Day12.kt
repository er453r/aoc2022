fun main() {
    val aCode = 'a'.code
    val startCode = 'S'.code - aCode
    val endCode = 'E'.code - aCode

    fun findPathLength(start:GridCell<Int>, end:GridCell<Int>, grid:Grid<Int>):Int{
        fun h(cell: GridCell<Int>): Int = (end.position - cell.position).length()

        val openSet = mutableSetOf(start)
        val gScores = mutableMapOf(start to 0)
        val fScores = mutableMapOf(start to h(start))
        val cameFrom = mutableMapOf<GridCell<Int>, GridCell<Int>>()

        fun reconstructPath(cameFrom: Map<GridCell<Int>, GridCell<Int>>, end: GridCell<Int>): List<GridCell<Int>> {
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
                return reconstructPath(cameFrom, current).size - 1

            openSet.remove(current)

            val neighbours = grid.crossNeighbours(current.position).filter {
                it.value < current.value + 2
            }

            for (neighbour in neighbours) {
                val neighbourScore = gScores[current]!! + 1

                if (neighbourScore < gScores.getOrDefault(neighbour, 999999)) {
                    cameFrom[neighbour] = current
                    gScores[neighbour] = neighbourScore
                    fScores[neighbour] = neighbourScore + h(neighbour)

                    if (neighbour !in openSet)
                        openSet += neighbour
                }

            }
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().asList().map { it.code - aCode } })

        val start = grid.data.flatten().first { it.value == startCode }
        val end = grid.data.flatten().first { it.value == endCode }
        start.value = 0
        end.value = 26

        return findPathLength(start, end, grid)
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().asList().map { it.code - aCode } })

        val start = grid.data.flatten().first { it.value == startCode }
        val end = grid.data.flatten().first { it.value == endCode }
        start.value = 0
        end.value = 26

        return grid.data.flatten().filter { it.value == 0 }.map { findPathLength(it, end, grid) }.filter { it != -1 }.min()
    }

    test(
        day = 12,
        testTarget1 = 31,
        testTarget2 = 29,
        part1 = ::part1,
        part2 = ::part2,
    )
}
