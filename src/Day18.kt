fun main() {
    fun part1(input: List<String>): Int {
        val occupied = input.map { it.ints() }.map { (x, y, z) -> Vector3d(x, y, z) }.toSet()

        return occupied.sumOf { cube -> 6 - Vector3d.DIRECTIONS.count { dir -> cube + dir in occupied } }
    }

    fun part2(input: List<String>): Int {
        val occupied = input.map { it.ints() }.map { (x, y, z) -> Vector3d(x, y, z) }.toMutableSet()

        val minX = occupied.minOf { it.x } - 1
        val minY = occupied.minOf { it.y } - 1
        val minZ = occupied.minOf { it.z } - 1
        val maxX = occupied.maxOf { it.x } + 1
        val maxY = occupied.maxOf { it.y } + 1
        val maxZ = occupied.maxOf { it.z } + 1

        val xRange = minX..maxX
        val yRange = minY..maxY
        val zRange = minZ..maxZ

        val all = xRange.map { x -> yRange.map { y -> zRange.map { z -> Vector3d(x, y, z) } } }.flatten().flatten().toSet()
        val airOutside = mutableSetOf<Vector3d>()
        val visitQueue = mutableListOf(Vector3d(minX, minY, minZ))

        while (visitQueue.isNotEmpty()) { // fill the outside with air!
            val candidate = visitQueue.removeLast()

            airOutside += candidate

            for (direction in Vector3d.DIRECTIONS) {
                val next = candidate + direction

                if (next in airOutside || next in occupied || next.x !in xRange || next.y !in yRange || next.z !in zRange)
                    continue

                visitQueue += next
            }
        }

        val airPockets = all - airOutside - occupied

        occupied += airPockets

        return occupied.sumOf { cube -> 6 - Vector3d.DIRECTIONS.count { dir -> cube + dir in occupied } }
    }

    test(
        day = 18,
        testTarget1 = 64,
        testTarget2 = 58,
        part1 = ::part1,
        part2 = ::part2,
    )
}
