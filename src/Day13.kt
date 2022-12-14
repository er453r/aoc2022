fun main() {
    data class ListOrValue(val value: Int? = null, val list: List<ListOrValue>? = null)

    fun parseLine(line: String): ListOrValue = when {
        line.startsWith("[") -> { // find the end
            val list = mutableListOf<ListOrValue>()
            var stackSize = 0
            var accumulator = ""

            for (n in 1 until line.length - 1) {
                accumulator += line[n]

                when {
                    line[n] == '[' -> stackSize++
                    line[n] == ']' -> stackSize--
                    line[n] == ',' && stackSize == 0 -> {
                        list.add(parseLine(accumulator.dropLast(1)))
                        accumulator = ""
                    }
                }
            }

            if (accumulator.isNotBlank())
                list.add(parseLine(accumulator))

            ListOrValue(list = list)
        }

        else -> ListOrValue(value = line.toInt())
    }

    fun compare(left: ListOrValue, right: ListOrValue): Int {
        if (left.value != null && right.value != null) // both are ints
            return left.value.compareTo(right.value)
        else if (left.list != null && right.list != null) {
            for (n in 0 until left.list.size) {
                if (right.list.size < n + 1)
                    return 1

                when (val itemComparison = compare(left.list[n], right.list[n])) {
                    0 -> continue
                    else -> return itemComparison
                }
            }

            if (left.list.size < right.list.size)
                return -1

            return 0
        } else {
            val leftConverted = if (left.value != null) ListOrValue(list = listOf(left)) else left
            val rightConverted = if (right.value != null) ListOrValue(list = listOf(right)) else right

            return compare(leftConverted, rightConverted)
        }
    }

    fun compareStrings(left: String, right: String) = compare(parseLine(left), parseLine(right))

    fun part1(input: List<String>) = input.chunked(3)
        .mapIndexed { index, lines -> Pair(index, compare(parseLine(lines[0]), parseLine(lines[1]))) }
        .filter { it.second < 0 }
        .sumOf { it.first + 1 }

    val dividers = arrayOf("[[2]]", "[[6]]")

    fun part2(input: List<String>) = (input + dividers).asSequence()
        .filter { it.isNotBlank() }
        .sortedWith(::compareStrings)
        .mapIndexed { index, line -> Pair(index + 1, line) }
        .filter { it.second in dividers }
        .map { it.first }
        .reduce(Int::times)

    test(
        day = 13,
        testTarget1 = 13,
        testTarget2 = 140,
        part1 = ::part1,
        part2 = ::part2,
    )
}
