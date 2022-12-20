import java.lang.Integer.max

fun main() {
    fun part1(input: List<String>): Int {
        val blueprints = input.map {line ->
            line.ints().let {
                listOf(
                    VectorN(it[1], 0, 0, 0),
                    VectorN(it[2], 0, 0, 0),
                    VectorN(it[3], it[4], 0, 0),
                    VectorN(it[5], 0, it[6], 0),
                )
            }
        }

        val timeLimit = 24

        fun decision(budget:VectorN, robots:VectorN, minutes:Int, blueprint:List<VectorN>):Int{
            if(minutes > timeLimit || robots.components[2] > blueprint[3].components[2] || robots.components[1] > blueprint[2].components[1]) {
//                println("-------")
//                println(budget)
//                println(robots)
                return budget.components[3]
            }

            // robots we can afford
            val robotsWeCanAfford = blueprint.mapIndexed { index, it -> index to ((budget - it).components.min() >= 0) }.filter { (_, canAfford) -> canAfford}.map { it.first }

            val withoutBuilding = decision(budget + robots, robots, minutes + 1, blueprint)

            if(robotsWeCanAfford.isNotEmpty()){
                val withBuilding = robotsWeCanAfford.maxOf {robotIndex ->
                    decision(budget + robots - blueprint[robotIndex], robots + VectorN(0,0,0,0).also { it.components[robotIndex] = 1 }, minutes + 1, blueprint)
                }

                return max(withBuilding, withoutBuilding)
            }

            return withoutBuilding
        }

        return blueprints.mapIndexed { index, blueprint ->
            val budget = VectorN(0, 0, 0, 0) // ore, clay, obsidian, geode
            val robots = VectorN(1, 0, 0, 0)

           val maxGeodes = decision(budget,robots, 1, blueprint)

            println("Blueprint $index produces max $maxGeodes geodes")

            (index + 1) * maxGeodes
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    test(
        day = 19,
        testTarget1 = 33,
        testTarget2 = 6,
        part1 = ::part1,
        part2 = ::part2,
    )
}
