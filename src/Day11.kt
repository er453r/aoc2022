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

    fun part1(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()
        val monkeyInspections = mutableMapOf<Int, Int>()

        input.chunked(7).forEach {
            monkeys.add(
                Monkey(
                    id = inputLineRegex.findAll(it[0]).map { it.value.toInt() }.first(),
                    items = ArrayDeque(inputLineRegex.findAll(it[1]).map { it.value.toLong() }.toList()),
                    operation = it[2].split("= old").last()[1],
                    operationParam = it[2].split("= old").last().substring(3),
                    testParam = inputLineRegex.findAll(it[3]).map { it.value.toInt() }.first(),
                    onSuccessTarget = inputLineRegex.findAll(it[4]).map { it.value.toInt() }.first(),
                    onFailedTarget = inputLineRegex.findAll(it[5]).map { it.value.toInt() }.first(),
                )
            )

            monkeyInspections[inputLineRegex.findAll(it[0]).map { it.value.toInt() }.first()] = 0
        }


        repeat(20) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val worryLevel = monkey.items.removeFirst()

                    monkeyInspections[monkey.id] = monkeyInspections[monkey.id]!! + 1

                    val operationParameter: Long = if (monkey.operationParam == "old") worryLevel else monkey.operationParam.toLong()

                    var newWorryLevel = when (monkey.operation) {
                        '*' -> worryLevel * operationParameter
                        '/' -> worryLevel / operationParameter
                        '+' -> worryLevel + operationParameter
                        '-' -> worryLevel - operationParameter
                        else -> throw Exception("Unknown operation $monkey.operation")
                    }

                    newWorryLevel /= 3

                    if (newWorryLevel % monkey.testParam == 0L)
                        monkeys[monkey.onSuccessTarget].items.addLast(newWorryLevel)
                    else
                        monkeys[monkey.onFailedTarget].items.addLast(newWorryLevel)
                }
            }
        }

        println(monkeyInspections)

        return monkeyInspections.values.sorted().takeLast(2).map { it.toLong() }.reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()
        val monkeyInspections = mutableMapOf<Int, Int>()

        input.chunked(7).forEach {
            monkeys.add(
                Monkey(
                    id = inputLineRegex.findAll(it[0]).map { it.value.toInt() }.first(),
                    items = ArrayDeque(inputLineRegex.findAll(it[1]).map { it.value.toLong() }.toList()),
                    operation = it[2].split("= old").last()[1],
                    operationParam = it[2].split("= old").last().substring(3),
                    testParam = inputLineRegex.findAll(it[3]).map { it.value.toInt() }.first(),
                    onSuccessTarget = inputLineRegex.findAll(it[4]).map { it.value.toInt() }.first(),
                    onFailedTarget = inputLineRegex.findAll(it[5]).map { it.value.toInt() }.first(),
                )
            )

            monkeyInspections[inputLineRegex.findAll(it[0]).map { it.value.toInt() }.first()] = 0
        }

        println("Got ${monkeys.size} monkeys")

        monkeys.forEach { println("${it.id} - ${it.testParam}") }

        val maxDiv = monkeys.map { it.testParam }.toSet().reduce(Int::times)

        repeat(10000) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val worryLevel = monkey.items.removeFirst()

                    monkeyInspections[monkey.id] = monkeyInspections[monkey.id]!! + 1

                    val operationParameter: Long = if (monkey.operationParam == "old") worryLevel else monkey.operationParam.toLong()

                    var newWorryLevel = when (monkey.operation) {
                        '*' -> worryLevel * operationParameter
                        '/' -> worryLevel / operationParameter
                        '+' -> worryLevel + operationParameter
                        '-' -> worryLevel - operationParameter
                        else -> throw Exception("Unknown operation $monkey.operation")
                    } % maxDiv

                    if (newWorryLevel % monkey.testParam == 0L)
                        monkeys[monkey.onSuccessTarget].items.addLast(newWorryLevel)
                    else
                        monkeys[monkey.onFailedTarget].items.addLast(newWorryLevel)
                }
            }

            if(it <= 20 - 1)
                println(monkeyInspections)
        }

        return monkeyInspections.values.sorted().takeLast(2).map { it.toLong() }.reduce(Long::times)
    }

    test(
        day = 11,
        testTarget1 = 10605,
        testTarget2 = 2713310158,
        part1 = ::part1,
        part2 = ::part2,
    )
}
