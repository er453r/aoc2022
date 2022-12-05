fun main() {
    val inputLineRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

    fun fillStacks(stacks: List<ArrayDeque<Char>>, line: String) {
        stacks.forEachIndexed { index, chars ->
            line.getOrNull(index * 4 + 1)?.takeIf { it.isLetter() }?.let {
                chars.addLast(it)
            }
        }
    }

    fun part1(input: List<String>): String {
        val stacks = List(9) { ArrayDeque<Char>() }

        for (line in input) {
            if (line.contains('['))
                fillStacks(stacks, line)

            if (line.contains("move")) {
                val (count, from, to) = line.destructured(inputLineRegex)

                for (i in 0 until count.toInt())
                    stacks[to.toInt() - 1].addFirst(stacks[from.toInt() - 1].removeFirst())
            }
        }

        return stacks.map { if (it.isEmpty()) "" else it.first() }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val stacks = List(9) { ArrayDeque<Char>() }

        for (line in input) {
            if (line.contains('['))
                fillStacks(stacks, line)

            if (line.contains("move")) {
                val (count, from, to) = line.destructured(inputLineRegex)

                val queue = ArrayDeque<Char>()

                for (i in 0 until count.toInt())
                    queue.add(stacks[from.toInt() - 1].removeFirst())

                while (queue.isNotEmpty())
                    stacks[to.toInt() - 1].addFirst(queue.removeLast())
            }
        }

        return stacks.map { if (it.isEmpty()) "" else it.first() }.joinToString(separator = "")
    }

    test(
        day = 5,
        testTarget1 = "CMZ",
        testTarget2 = "MCD",
        part1 = ::part1,
        part2 = ::part2,
    )
}
