fun main() {
    fun fallingSand(input: List<String>, hardFloor: Boolean, stopOnOverflow: Boolean, startPoint: Vector2d = Vector2d(500, 0)): Int {
        val lines = input.map {
            it.ints().chunked(2).map { (x, y) ->
                Vector2d(x, y)
            }.toList()
        }

        val xMin = lines.flatten().toMutableList().also { it.add(startPoint) }.minOf { it.x }
        val yMin = lines.flatten().toMutableList().also { it.add(startPoint) }.minOf { it.y }
        val xMax = lines.flatten().toMutableList().also { it.add(startPoint) }.maxOf { it.x }
        val yMax = lines.flatten().toMutableList().also { it.add(startPoint) }.maxOf { it.y }

        val occupied = mutableSetOf<Vector2d>()

        for (line in lines)
            for (n in 0 until line.size - 1) {
                var current = line[n]
                val to = line[n + 1]
                val step = (to - current).normalized()

                while (current != to + step) {
                    occupied += current
                    current += step
                }
            }

        var unitsResting = 0

        unitLoop@ while (true) {
            var current = startPoint

            while (true) {
                when {
                    hardFloor && current.y == yMax + 1 -> {
                        occupied += current
                        unitsResting++

                        continue@unitLoop
                    }

                    !hardFloor && (current.x !in (xMin..xMax) || current.y !in (yMin..yMax)) -> return unitsResting
                    (current + Vector2d.DOWN) !in occupied -> current += Vector2d.DOWN
                    (current + Vector2d.DOWN + Vector2d.LEFT) !in occupied -> current += Vector2d.DOWN + Vector2d.LEFT
                    (current + Vector2d.DOWN + Vector2d.RIGHT) !in occupied -> current += Vector2d.DOWN + Vector2d.RIGHT
                    else -> {
                        occupied += current
                        unitsResting++

                        if (stopOnOverflow && current == startPoint)
                            return unitsResting

                        continue@unitLoop
                    }
                }
            }
        }
    }

    fun part1(input: List<String>) = fallingSand(input, hardFloor = false, stopOnOverflow = false)

    fun part2(input: List<String>) = fallingSand(input, hardFloor = true, stopOnOverflow = true)

    test(
        day = 14,
        testTarget1 = 24,
        testTarget2 = 93,
        part1 = ::part1,
        part2 = ::part2,
    )
}
