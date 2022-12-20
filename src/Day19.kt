import java.lang.Integer.max

fun main() {
    fun part1(input: List<String>): Int {
        val blueprints = input.map { line ->
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

        var bestForMinute = Array<Int>(30) { 0 }

        fun decision(budget: VectorN, robots: VectorN, minutes: Int, blueprint: List<VectorN>, maxBudget: VectorN, robotsWeDidNotBuild:Set<Int> = emptySet()): Int {
            if (minutes > timeLimit) { // end condition
                return budget.components[3]
            }

            // pruning
            if (budget.components[3] > bestForMinute[minutes])
                bestForMinute[minutes] = budget.components[3]

            if (bestForMinute[minutes] > budget.components[3])
                return budget.components[3]

            // robots we can afford
            val robotsWeCanAfford = blueprint.mapIndexed { index, it -> index to ((budget - it).components.min() >= 0) }.filter { (_, canAfford) -> canAfford }.map { it.first }.toSet()

            // we need to buy geode if we can afford it
            if (robotsWeCanAfford.contains(3))
                return decision(budget + robots - blueprint[3], robots + VectorN(0, 0, 0, 1), minutes + 1, blueprint, maxBudget)

            val robotsItMakesSenseToMake = robotsWeCanAfford.filter { robotIndex ->
                when{
                    robotIndex in robotsWeDidNotBuild -> false
                    (robots.components[robotIndex] >= maxBudget.components[robotIndex]) -> false
                    else -> true
                }
            }.toSet()

            val withoutBuilding = decision(budget + robots, robots, minutes + 1, blueprint, maxBudget, robotsItMakesSenseToMake)

            if (robotsItMakesSenseToMake.isNotEmpty()) {
                val withBuilding = robotsItMakesSenseToMake.maxOf { robotIndex ->
                    decision(budget + robots - blueprint[robotIndex], robots + VectorN(0, 0, 0, 0).also { it.components[robotIndex] = 1 }, minutes + 1, blueprint, maxBudget)
                }

                return max(withBuilding, withoutBuilding)
            }

            return withoutBuilding
        }

        return blueprints.mapIndexed { index, blueprint ->
            bestForMinute = Array<Int>(30) { 0 }
            val budget = VectorN(0, 0, 0, 0) // ore, clay, obsidian, geode
            val robots = VectorN(1, 0, 0, 0)

            val maxBudget = VectorN((0..3).map { c -> blueprint.maxOf { it.components[c] } }.toMutableList())

            println("maxBudget $maxBudget")

            val maxGeodes = decision(budget, robots, 1, blueprint, maxBudget)

            println("Blueprint $index produces max $maxGeodes geodes")

            (index + 1) * maxGeodes
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val blueprints = input.map { line ->
            line.ints().let {
                listOf(
                    VectorN(it[1], 0, 0, 0),
                    VectorN(it[2], 0, 0, 0),
                    VectorN(it[3], it[4], 0, 0),
                    VectorN(it[5], 0, it[6], 0),
                )
            }
        }.take(3)

        val timeLimit = 32

        var bestForMinute = Array<Int>(33) { 0 }

        fun decision(budget: VectorN, robots: VectorN, minutes: Int, blueprint: List<VectorN>, maxBudget: VectorN, robotsWeDidNotBuild:Set<Int> = emptySet()): Int {
            if (minutes > timeLimit) { // end condition
                return budget.components[3]
            }

            // pruning
            if (budget.components[3] > bestForMinute[minutes])
                bestForMinute[minutes] = budget.components[3]

            if (bestForMinute[minutes] > budget.components[3])
                return budget.components[3]

            // robots we can afford
            val robotsWeCanAfford = blueprint.mapIndexed { index, it -> index to ((budget - it).components.min() >= 0) }.filter { (_, canAfford) -> canAfford }.map { it.first }.toSet()

            // we need to buy geode if we can afford it
            if (robotsWeCanAfford.contains(3))
                return decision(budget + robots - blueprint[3], robots + VectorN(0, 0, 0, 1), minutes + 1, blueprint, maxBudget)

            val robotsItMakesSenseToMake = robotsWeCanAfford.filter { robotIndex ->
                when{
                    robotIndex in robotsWeDidNotBuild -> false
                    (robots.components[robotIndex] >= maxBudget.components[robotIndex]) -> false
                    else -> true
                }
            }.toSet()

            val withoutBuilding = decision(budget + robots, robots, minutes + 1, blueprint, maxBudget, robotsItMakesSenseToMake)

            if (robotsItMakesSenseToMake.isNotEmpty()) {
                val withBuilding = robotsItMakesSenseToMake.maxOf { robotIndex ->
                    decision(budget + robots - blueprint[robotIndex], robots + VectorN(0, 0, 0, 0).also { it.components[robotIndex] = 1 }, minutes + 1, blueprint, maxBudget)
                }

                return max(withBuilding, withoutBuilding)
            }

            return withoutBuilding
        }

        return blueprints.mapIndexed { index, blueprint ->
            bestForMinute = Array<Int>(33) { 0 }
            val budget = VectorN(0, 0, 0, 0) // ore, clay, obsidian, geode
            val robots = VectorN(1, 0, 0, 0)

            val maxBudget = VectorN((0..3).map { c -> blueprint.maxOf { it.components[c] } }.toMutableList())

            println("maxBudget $maxBudget")

            val maxGeodes = decision(budget, robots, 1, blueprint, maxBudget)

            println("Blueprint $index produces max $maxGeodes geodes")

            maxGeodes
        }.reduce(Int::times)
    }

    test(
        day = 19,
        testTarget1 = 33,
        testTarget2 = 3472, // 56 * 62
        part1 = ::part1,
        part2 = ::part2,
    )
}
