fun main() {
    val aCode = 'a'.code
    val (startCode, endCode) = arrayOf('S'.code - aCode, 'E'.code - aCode)

    fun neighbours(cell: GridCell<Int>, grid: Grid<Int>) = grid.crossNeighbours(cell.position).filter {
        it.value < cell.value + 2
    }

    fun prepareData(input: List<String>): Triple<Grid<Int>, GridCell<Int>, GridCell<Int>> {
        val grid = Grid(input.map { it.toCharArray().asList().map { char -> char.code - aCode } })
        val start = grid.data.flatten().first { it.value == startCode }.also { it.value = 0 }
        val end = grid.data.flatten().first { it.value == endCode }.also { it.value = 26 }

        return Triple(grid, start, end)
    }

    fun part1(input: List<String>) = prepareData(input).let { (grid, start, end) ->
        grid.path(start, end, neighbours = { neighbours(it, grid) }).size - 1
    }

    fun part2(input: List<String>) = prepareData(input).let { (grid, _, end) ->
        grid.data.asSequence().flatten().filter { it.value == 0 }.map { cell ->
            grid.path(cell, end, neighbours = { neighbours(it, grid) })
        }.filter { it.isNotEmpty() }.minOf { it.size - 1 }
    }

    test(
        day = 12,
        testTarget1 = 31,
        testTarget2 = 29,
        part1 = ::part1,
        part2 = ::part2,
    )
}
