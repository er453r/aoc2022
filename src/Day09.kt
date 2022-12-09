import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val visited = mutableSetOf<String>()

        var head = Vector2d(0,0)
        var tail = Vector2d(0,0)

        visited.add(tail.toString())

        for(command in input){
            val (direction, steps) = command.split(" ")

            repeat(steps.toInt()){
                head += when(direction){
                    "U" -> Vector2d.UP
                    "D" -> Vector2d.DOWN
                    "L" -> Vector2d.LEFT
                    "R" -> Vector2d.RIGHT
                    else -> throw Exception("Unknown command $it")
                }

                val diff = head - tail

                if(abs(diff.x) > 1 || abs(diff.y) > 1){ // we need to move
                    val normX = if (diff.x != 0) diff.x / abs(diff.x) else 0
                    val normY = if (diff.y != 0) diff.y / abs(diff.y) else 0

                    tail += Vector2d(normX, normY)

                    visited.add(tail.toString())
                }
            }
        }

        return visited.size
    }

    fun part2(input: List<String>): Int {
        val visited = mutableSetOf<String>()

        var knots = Array<Vector2d>(10){Vector2d(0,0)}

        visited.add(knots.last().toString())

        for(command in input){
            val (direction, steps) = command.split(" ")

            repeat(steps.toInt()){
                knots[0] += when(direction){
                    "U" -> Vector2d.UP
                    "D" -> Vector2d.DOWN
                    "L" -> Vector2d.LEFT
                    "R" -> Vector2d.RIGHT
                    else -> throw Exception("Unknown command $it")
                }

                for(n in 1 until knots.size){
                    val diff = knots[n -1] - knots[n]

                    if(abs(diff.x) > 1 || abs(diff.y) > 1){ // we need to move
                        val normX = if (diff.x != 0) diff.x / abs(diff.x) else 0
                        val normY = if (diff.y != 0) diff.y / abs(diff.y) else 0

                        knots[n] += Vector2d(normX, normY)

                        if(n == knots.size - 1)
                            visited.add(knots[n].toString())
                    }
                }
            }
        }

        return visited.size
    }

    test(
        day = 9,
        testTarget1 = 13,
        testTarget2 = 1,
        part1 = ::part1,
        part2 = ::part2,
    )
}
