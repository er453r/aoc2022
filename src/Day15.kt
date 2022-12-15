import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val coverage = mutableSetOf<Vector2d>()
        val data = input.map { line ->
            line.ints().let {
                val sensor = Vector2d(it[0], it[1])
                val beacon = Vector2d(it[2], it[3])
                val range = (sensor - beacon).manhattan()

                Triple(sensor, beacon, range)
            }
        }

        val xMin = data.minOf { (sensor, _, range) -> sensor.x - range }
        val xMax = data.maxOf { (sensor, _, range) -> sensor.x + range }

        val targetY = 2000000

        for (x in xMin..xMax) {
            for ((sensor, _, range) in data) {
                val candidate = Vector2d(x, targetY)

                if ((sensor - candidate).manhattan() <= range)
                    coverage += candidate
            }
        }

        coverage -= data.map { (_, beacon, _) -> beacon }.toSet()
        coverage -= data.map { (sensor, _, _) -> sensor }.toSet()

        return coverage.size.toLong()
    }

    fun part2(input: List<String>): Long {
        val data = input.map { line ->
            line.ints().let {
                val sensor = Vector2d(it[0], it[1])
                val beacon = Vector2d(it[2], it[3])
                val range = (sensor - beacon).manhattan()

                Triple(sensor, beacon, range)
            }
        }

        val bounds = mutableSetOf<Vector2d>()

//        val maxBound = 20
                val maxBound = 4000000

        for ((sensor, _, range) in data) {
//            println("SENSOR")
            val boundRange = range + 1
            var dx = 0

            for(y in max(sensor.y - boundRange, 0) ..  min(sensor.y, maxBound)){
                if(sensor.x + dx <= maxBound)
                    bounds += Vector2d(sensor.x + dx, y)

                if(sensor.x - dx >= 0)
                    bounds += Vector2d(sensor.x - dx, y)

                dx++
            }

            dx--

            for(y in max(sensor.y + 1, 0) .. min(sensor.y + boundRange, maxBound)){
                if(sensor.x + dx <= maxBound)
                    bounds += Vector2d(sensor.x + dx, y)

                if(sensor.x - dx >= 0)
                    bounds += Vector2d(sensor.x - dx, y)

                dx++
            }

//            println(bounds)
        }




        for(bound in bounds.filter { it.x in (0 .. maxBound) && it.y in (0 .. maxBound) }){
            if(data.count { (sensor, _, range) -> (sensor-bound).manhattan() <= range } == 0){
//                println(bound)
                return bound.x * 4000000L + bound.y
            }

        }

        return -1
    }

    test(
        day = 15,
        testTarget1 = 0,
        testTarget2 = 56000011,
        part1 = ::part1,
        part2 = ::part2,
    )
}
