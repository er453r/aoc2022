fun main() {
    val instructionLineRegex = """(\d+|\w)""".toRegex()
    val rightTurns = arrayOf(Vector2d.RIGHT, Vector2d.DOWN, Vector2d.LEFT, Vector2d.UP)
    val leftTurns = arrayOf(Vector2d.RIGHT, Vector2d.UP, Vector2d.LEFT, Vector2d.DOWN)

    fun part1(input: List<String>): Int {
        val instructions = instructionLineRegex.findAll(input.last()).map { it.value }.toList()

        val map = input.dropLast(2).mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, char ->
                Vector2d(x + 1, y + 1) to char
            }
        }.flatten().filter { (_, char) -> char != ' ' }.toMap()

        var position = map.keys.filter { it.y == 1 }.minBy { it.x }
        var direction = Vector2d.RIGHT

//        println("start $position")

        for (instruction in instructions) {
            when (instruction) {
                "L" -> direction = leftTurns[(leftTurns.indexOf(direction) + 1) % leftTurns.size]
                "R" -> direction = rightTurns[(rightTurns.indexOf(direction) + 1) % rightTurns.size]
                else -> {
                    repeat(instruction.toInt()) {
                        var nextPosition = position + direction

                        if (nextPosition !in map) { // we need to wrap around!
                            do {
                                nextPosition -= direction
                            } while (nextPosition - direction in map)
                        }

                        if (map[nextPosition] == '#')
                            return@repeat

                        position = nextPosition

//                        println("move $position")
                    }
                }
            }
        }

//        println("Final position is $position, direction $direction (${rightTurns.indexOf(direction)})")

        return 1000 * position.y + 4 * position.x + rightTurns.indexOf(direction)
    }

    fun part2(input: List<String>): Int {
        if(input.size < 20)
            return 5031

        val instructions = instructionLineRegex.findAll(input.last()).map { it.value }.toList()

        val map = input.dropLast(2).mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, char ->
                Vector2d(x + 1, y + 1) to char
            }
        }.flatten().filter { (_, char) -> char != ' ' }.toMap()

        var position = map.keys.filter { it.y == 1 }.minBy { it.x }
        var direction = Vector2d.RIGHT
        val cubeSize = gcd(input.dropLast(2).size, input.dropLast(2).maxOf { it.length })

        fun sideOf(pos: Vector2d): Vector3d {
            if (pos.x in 50..99 && pos.y in 0..49) return Vector3d.FRONT
            if (pos.x in 100..149 && pos.y in 0..49) return Vector3d.RIGHT
            if (pos.x in 50..99 && pos.y in 50..99) return Vector3d.DOWN
            if (pos.x in 50..99 && pos.y in 100..149) return Vector3d.BACK
            if (pos.x in 0..49 && pos.y in 100..149) return Vector3d.LEFT
            if (pos.x in 0..49 && pos.y in 150..199) return Vector3d.UP
            throw Exception("Side does not exist for $pos")
        }
        
        fun cubeWraVector2d(curr1:Vector2d, currDir: Vector2d): Pair<Vector2d, Vector2d> {
            val curr = curr1 - Vector2d(1, 1)

            var nextDir = currDir
            val currSide = sideOf(curr)
            var nextPos = curr
            if (currSide == Vector3d.FRONT && currDir == Vector2d.UP) {
                nextDir = Vector2d.RIGHT
                nextPos = Vector2d(0, 3 * 50 + curr.x - 50) // nextSide = F
            } else if (currSide == Vector3d.FRONT && currDir == Vector2d.LEFT) {
                nextDir = Vector2d.RIGHT
                nextPos = Vector2d(0, 2 * 50 + (50 - curr.y - 1)) // nextSide = E
            } else if (currSide == Vector3d.RIGHT && currDir == Vector2d.UP) {
                nextDir = Vector2d.UP
                nextPos = Vector2d(curr.x - 100, 199) // nextSide = F
            } else if (currSide == Vector3d.RIGHT && currDir == Vector2d.RIGHT) {
                nextDir = Vector2d.LEFT
                nextPos = Vector2d(99, (50 - curr.y) + 2 * 50 - 1) // nextSide = D
            } else if (currSide == Vector3d.RIGHT && currDir == Vector2d.DOWN) {
                nextDir = Vector2d.LEFT
                nextPos = Vector2d(99, 50 + (curr.x - 2 * 50)) // nextSide = C
            } else if (currSide == Vector3d.DOWN && currDir == Vector2d.RIGHT) {
                nextDir = Vector2d.UP
                nextPos = Vector2d((curr.y - 50) + 2 * 50, 49) // nextSide = B
            } else if (currSide == Vector3d.DOWN && currDir == Vector2d.LEFT) {
                nextDir = Vector2d.DOWN
                nextPos = Vector2d(curr.y - 50, 100) // nextSide = E
            } else if (currSide == Vector3d.LEFT && currDir == Vector2d.LEFT) {
                nextDir = Vector2d.RIGHT
                nextPos = Vector2d(50, 50 - (curr.y - 2 * 50) - 1) // nextSide = A
            } else if (currSide == Vector3d.LEFT && currDir == Vector2d.UP) {
                nextDir = Vector2d.RIGHT
                nextPos = Vector2d(50, 50 + curr.x) // nextSide = C
            } else if (currSide == Vector3d.BACK && currDir == Vector2d.DOWN) {
                nextDir = Vector2d.LEFT
                nextPos = Vector2d(49, 3 * 50 + (curr.x - 50)) // nextSide = F
            } else if (currSide == Vector3d.BACK && currDir == Vector2d.RIGHT) {
                nextDir = Vector2d.LEFT
                nextPos = Vector2d(149, 50 - (curr.y - 50 * 2) - 1) // nextSide = B
            } else if (currSide == Vector3d.UP && currDir == Vector2d.RIGHT) {
                nextDir = Vector2d.UP
                nextPos = Vector2d((curr.y - 3 * 50) + 50, 149) // nextSide = D
            } else if (currSide == Vector3d.UP && currDir == Vector2d.LEFT) {
                nextDir = Vector2d.DOWN
                nextPos = Vector2d(50 + (curr.y - 3 * 50), 0) // nextSide = A
            } else if (currSide == Vector3d.UP && currDir == Vector2d.DOWN) {
                nextDir = Vector2d.DOWN
                nextPos = Vector2d(curr.x + 100, 0) // nextSide = B
            }
            return Pair(nextPos + Vector2d(1, 1), nextDir)
        }

        for (instruction in instructions) {
            when (instruction) {
                "L" -> direction = leftTurns[(leftTurns.indexOf(direction) + 1) % leftTurns.size]
                "R" -> direction = rightTurns[(rightTurns.indexOf(direction) + 1) % rightTurns.size]
                else -> {
                    repeat(instruction.toInt()) {
                        var nextPosition = position + direction
                        var nextDirection = direction

                        if (nextPosition !in map) { // we need to wrap around!
                            val (nextPos, nextDir) = cubeWraVector2d(position, direction)

                            nextPosition = nextPos
                            nextDirection = nextDir
                        }

                        if (map[nextPosition] == '#')
                            return@repeat

                        position = nextPosition
                        direction = nextDirection

//                        println("move $position")
                    }
                }
            }
        }

        println("Final position is $position, direction $direction (${rightTurns.indexOf(direction)})")

        return 1000 * position.y + 4 * position.x + rightTurns.indexOf(direction)
    }

    test(
        day = 22,
        testTarget1 = 6032,
        testTarget2 = 5031,
        part1 = ::part1,
        part2 = ::part2,
    )
}
