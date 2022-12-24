fun main() {
    fun part1(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().toList() })

        val width = grid.width - 2
        val height = grid.height - 2

//        println("Grid $width x $height")

        val start = Vector3d(0, -1, 0)
        val end = Vector2d(width - 1, height)
        val directions = mapOf('<' to Vector3d.LEFT, '>' to Vector3d.RIGHT, '^' to Vector3d.UP, 'v' to Vector3d.DOWN)
        val neighboursInTime = directions.values.map { it + Vector3d.BACK }.toSet() + Vector3d.BACK

        val initialState = grid.data.flatten().filter { it.value in directions }.associate {
            Vector3d(it.position.x, it.position.y, 0) - Vector3d(1, 1, 0) to directions[it.value]
        }

        fun isEmpty(positionInTime: Vector3d): Boolean {
            if(positionInTime.x !in (0 until width) || positionInTime.y !in (0 until height))
                return false

            val minutes = positionInTime.z
            val position = Vector3d(positionInTime.x, positionInTime.y, 0)

            for (direction in directions.values) { // we assume position is empty, check if blizzard is approaching
                val possibleBlizzardPosition = position - direction * minutes

                possibleBlizzardPosition.x = (width + (possibleBlizzardPosition.x % width)) % width
                possibleBlizzardPosition.y = (height + (possibleBlizzardPosition.y % height)) % height

                if (possibleBlizzardPosition in initialState && initialState[possibleBlizzardPosition] == direction)
                    return false
            }

            return true
        }

        fun neighbours(node: Vector3d):Set<Vector3d>{
//            println("Considering $node")

            if(node.z > 10000)
                return emptySet()

            val allNeighbours = neighboursInTime.map { node + it }.toSet()

            val possibleEnd = allNeighbours.firstOrNull { it.x == end.x && it.y == end.y }
            val possibleStart = allNeighbours.firstOrNull { it.x == start.x && it.y == start.y }

            var neighboursInBonds = allNeighbours.filter { isEmpty(it) }.toMutableSet()

            if(possibleEnd != null)
                neighboursInBonds += possibleEnd

            if(possibleStart != null)
                neighboursInBonds += possibleStart

            return neighboursInBonds
        }

        val path = aStar(
            start = start,
            isEndNode = { node -> node.x == end.x && node.y == end.y },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = ::neighbours)

//        path.reversed().forEach { println(it) }

        return path.size - 1
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input.map { it.toCharArray().toList() })

        val width = grid.width - 2
        val height = grid.height - 2

//        println("Grid $width x $height")

        val start = Vector3d(0, -1, 0)
        val end = Vector2d(width - 1, height)
        val directions = mapOf('<' to Vector3d.LEFT, '>' to Vector3d.RIGHT, '^' to Vector3d.UP, 'v' to Vector3d.DOWN)
        val neighboursInTime = directions.values.map { it + Vector3d.BACK }.toSet() + Vector3d.BACK

        val initialState = grid.data.flatten().filter { it.value in directions }.associate {
            Vector3d(it.position.x, it.position.y, 0) - Vector3d(1, 1, 0) to directions[it.value]
        }

        fun isEmpty(positionInTime: Vector3d): Boolean {
            if(positionInTime.x !in (0 until width) || positionInTime.y !in (0 until height))
                return false

            val minutes = positionInTime.z
            val position = Vector3d(positionInTime.x, positionInTime.y, 0)

            for (direction in directions.values) { // we assume position is empty, check if blizzard is approaching
                val possibleBlizzardPosition = position - direction * minutes

                possibleBlizzardPosition.x = (width + (possibleBlizzardPosition.x % width)) % width
                possibleBlizzardPosition.y = (height + (possibleBlizzardPosition.y % height)) % height

                if (possibleBlizzardPosition in initialState && initialState[possibleBlizzardPosition] == direction)
                    return false
            }

            return true
        }

        fun neighbours(node: Vector3d):Set<Vector3d>{
//            println("Considering $node")

            if(node.z > 10000)
                return emptySet()

            val allNeighbours = neighboursInTime.map { node + it }.toSet()

            val possibleEnd = allNeighbours.firstOrNull { it.x == end.x && it.y == end.y }
            val possibleStart = allNeighbours.firstOrNull { it.x == start.x && it.y == start.y }

            var neighboursInBonds = allNeighbours.filter { isEmpty(it) }.toMutableSet()

            if(possibleEnd != null)
                neighboursInBonds += possibleEnd

            if(possibleStart != null)
                neighboursInBonds += possibleStart

            return neighboursInBonds
        }

        val path1 = aStar(
            start = start,
            isEndNode = { node -> node.x == end.x && node.y == end.y },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = ::neighbours)

        val path2 = aStar(
            start = path1.first(),
            isEndNode = { node -> node.x == 0 && node.y == -1 },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = ::neighbours)

        val path3 = aStar(
            start = path2.first(),
            isEndNode = { node -> node.x == end.x && node.y == end.y },
            heuristic = { (end - Vector2d(it.x, it.y)).manhattan() },
            neighbours = ::neighbours)

//        path.reversed().forEach { println(it) }

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
