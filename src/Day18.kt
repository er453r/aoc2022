fun main() {
    fun part1(input: List<String>): Int {
        val occuppied = input.map { it.ints() }.map { (x, y, z) -> Vector3d(x, y, z) }.toSet()

        var totalArea = 0

        for (cube in occuppied) {
            var cubeArea = 6

            for (direction in Vector3d.DIRECTIONS)
                if (cube + direction in occuppied)
                    cubeArea--

            totalArea += cubeArea
        }

        return totalArea
    }

    fun part2(input: List<String>): Int {
        var occuppied = input.map { it.ints() }.map { (x, y, z) -> Vector3d(x, y, z) }.toMutableSet()

        var totalArea = 0

        val minX = occuppied.minOf { it.x } - 1
        val minY = occuppied.minOf { it.y } - 1
        val minZ = occuppied.minOf { it.z } - 1
        val maxX = occuppied.maxOf { it.x } + 1
        val maxY = occuppied.maxOf { it.y } + 1
        val maxZ = occuppied.maxOf { it.z } + 1

        val xRange = minX..maxX
        val yRange = minY..maxY
        val zRange = minZ..maxZ

        val all = mutableSetOf<Vector3d>()

        for (x in xRange)
            for (y in yRange)
                for (z in zRange)
                    all += Vector3d(x, y, z)

        println("ALL ${all.size}")

        val airOutside = mutableSetOf<Vector3d>()
        val visitQueue = mutableListOf(Vector3d(minX, minY, minZ))

        while (visitQueue.isNotEmpty()) {
            val candidate = visitQueue.removeLast()

            airOutside += candidate

            for (direction in Vector3d.DIRECTIONS){
                val next = candidate + direction

                if(next in airOutside || next in occuppied || next.x !in xRange || next.y !in yRange || next.z !in zRange)
                    continue

                visitQueue += next
            }
        }

        val airPockets = all - airOutside - occuppied

        println("Air pockets $airPockets")

        occuppied += airPockets

        for (cube in occuppied) {
            var cubeArea = 6

            for (direction in Vector3d.DIRECTIONS)
                if (cube + direction in occuppied)
                    cubeArea--

            totalArea += cubeArea
        }

        return totalArea
    }

    test(
        day = 18,
        testTarget1 = 64,
        testTarget2 = 58,
        part1 = ::part1,
        part2 = ::part2,
    )
}
