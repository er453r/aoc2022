fun main() {
    val directions = mapOf('<' to Vector3d.LEFT, '>' to Vector3d.RIGHT, '^' to Vector3d.UP, 'v' to Vector3d.DOWN)
    val neighboursInTime = directions.values.map { it + Vector3d.BACK }.toSet() + Vector3d.BACK
    val start = Vector3d(0, -1, 0)

    fun isEmpty(positionInTime: Vector3d, width: Int, height: Int, initialState: Map<Vector3d, Vector3d?>, start: Vector3d, end: Vector2d) = when {
        (positionInTime.x == start.x && positionInTime.y == start.y) || (positionInTime.x == end.x && positionInTime.y == end.y) -> true // start or end is OK
        positionInTime.x !in (0 until width) || positionInTime.y !in (0 until height) -> false
        directions.values.map { direction ->
            Pair((Vector3d(positionInTime.x, positionInTime.y, 0) - direction * positionInTime.z).apply {
                x = (width + (x % width)) % width
                y = (height + (y % height)) % height
            }, direction)
        }.any { (possibleBlizzard, direction) -> possibleBlizzard in initialState && initialState[possibleBlizzard] == direction } -> false

        else -> true
    }

    fun neighbours(node: Vector3d, start: Vector3d, end: Vector2d, width: Int, height: Int, initialState: Map<Vector3d, Vector3d?>): Set<Vector3d> {
        return neighboursInTime.map { node + it }.filter { isEmpty(it, width, height, initialState, start, end) }.toSet()
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().toList() })
        val width = grid.width - 2
        val height = grid.height - 2
        val end = Vector2d(width - 1, height)

        val initialState = grid.data.flatten().filter { it.value in directions }.associate {
            Vector3d(it.position.x, it.position.y, 0) - Vector3d(1, 1, 0) to directions[it.value]
        }

        val path = aStar(
            start = start,
            isEndNode = { node -> node.x == end.x && node.y == end.y },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = { node ->
                neighbours(node, start, end, width, height, initialState)
            })

        return path.size - 1
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().toList() })
        val width = grid.width - 2
        val height = grid.height - 2
        val end = Vector2d(width - 1, height)

        val initialState = grid.data.flatten().filter { it.value in directions }.associate {
            Vector3d(it.position.x, it.position.y, 0) - Vector3d(1, 1, 0) to directions[it.value]
        }

        val path1 = aStar(
            start = start,
            isEndNode = { node -> node.x == end.x && node.y == end.y },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = { node ->
                neighbours(node, start, end, width, height, initialState)
            })

        val path2 = aStar(
            start = path1.first(),
            isEndNode = { node -> node.x == 0 && node.y == -1 },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = { node ->
                neighbours(node, start, end, width, height, initialState)
            })

        val path3 = aStar(
            start = path2.first(),
            isEndNode = { node -> node.x == end.x && node.y == end.y },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = { node ->
                neighbours(node, start, end, width, height, initialState)
            })

        return path1.size + path2.size + path3.size - 3
    }

    test(
        day = 24,
        testTarget1 = 18,
        testTarget2 = 54,
        part1 = ::part1,
        part2 = ::part2,
    )
}
