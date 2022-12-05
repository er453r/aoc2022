fun main() {
    val inputLineRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

    fun partX(input: List<String>, moveLogic: (List<ArrayDeque<Char>>, Int, Int, Int) -> Unit): String {
        val stacks = List(9) { ArrayDeque<Char>() }

        for (line in input) {
            if (line.contains('[')) {
                stacks.forEachIndexed { index, chars ->
                    line.getOrNull(index * 4 + 1)?.takeIf { it.isLetter() }?.let {
                        chars.addLast(it)
                    }
                }
            }

            if (line.contains("move")) {
                val (count, from, to) = line.destructured(inputLineRegex)

                moveLogic(stacks, count.toInt(), from.toInt(), to.toInt())
            }
        }

        return stacks.filter { it.isNotEmpty() }.map { it.first() }.joinToString(separator = "")
    }

    fun part1(input: List<String>) = partX(input) { stacks, count, from, to ->
        for (i in 0 until count)
            stacks[to - 1].addFirst(stacks[from - 1].removeFirst())
    }

    fun part2(input: List<String>) = partX(input) { stacks, count, from, to ->
        val queue = ArrayDeque<Char>()

        for (i in 0 until count)
            queue.add(stacks[from - 1].removeFirst())

        while (queue.isNotEmpty())
            stacks[to - 1].addFirst(queue.removeLast())
    }

    test(
        day = 5,
        testTarget1 = "CMZ",
        testTarget2 = "MCD",
        part1 = ::part1,
        part2 = ::part2,
    )
}
