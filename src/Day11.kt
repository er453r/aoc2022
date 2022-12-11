fun main() {
    data class Monkey(
        val id: Int,
        val items: ArrayDeque<Long>,
        val operation: Char,
        val operationParam: String,
        val testParam: Int,
        val onSuccessTarget: Int,
        val onFailedTarget: Int,
    )

    val inputLineRegex = """\d+""".toRegex()

    fun monkeyBusiness(input: List<String>, rounds: Int, divisor: Int): Long {
        val monkeys = mutableListOf<Monkey>()
        val inspections = mutableMapOf<Int, Int>()

        input.chunked(7).forEach { line ->
            Monkey(
                id = inputLineRegex.findAll(line[0]).map { it.value.toInt() }.first(),
                items = ArrayDeque(inputLineRegex.findAll(line[1]).map { it.value.toLong() }.toList()),
                operation = line[2].split("= old").last()[1],
                operationParam = line[2].split("= old").last().substring(3),
                testParam = inputLineRegex.findAll(line[3]).map { it.value.toInt() }.first(),
                onSuccessTarget = inputLineRegex.findAll(line[4]).map { it.value.toInt() }.first(),
                onFailedTarget = inputLineRegex.findAll(line[5]).map { it.value.toInt() }.first(),
            ).let {
                monkeys.add(it)
                inspections[it.id] = 0
            }
        }

        val maxDiv = monkeys.map { it.testParam }.toSet().reduce(Int::times)

        repeat(rounds) {
            monkeys.forEach { monkey ->
                inspections[monkey.id] = inspections[monkey.id]!! + monkey.items.size

                while (monkey.items.isNotEmpty()) {
                    val worryLevel = monkey.items.removeFirst()
                    val operationParameter: Long = if (monkey.operationParam == "old") worryLevel else monkey.operationParam.toLong()
                    val newWorryLevel = when (monkey.operation) {
                        '*' -> worryLevel * operationParameter
                        '+' -> worryLevel + operationParameter
                        else -> throw Exception("Unknown operation $monkey.operation")
                    } / divisor % maxDiv

                    monkeys[if (newWorryLevel % monkey.testParam == 0L) monkey.onSuccessTarget else monkey.onFailedTarget].items.addLast(newWorryLevel)
                }
            }
        }

        return inspections.values.sorted().takeLast(2).map { it.toLong() }.reduce(Long::times)
    }

    fun part1(input: List<String>) = monkeyBusiness(input, rounds = 20, divisor = 3)

    fun part2(input: List<String>) = monkeyBusiness(input, rounds = 10000, divisor = 1)

    test(
        day = 11,
        testTarget1 = 10605,
        testTarget2 = 2713310158L,
        part1 = ::part1,
        part2 = ::part2,
    )
}
