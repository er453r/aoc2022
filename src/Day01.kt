fun main() {
    fun sums(input: List<String>): List<Int> {
        val sums = mutableListOf<Int>()
        var sum = 0

        input.forEachIndexed { index, value ->
            if(value.isNotBlank())
                sum += value.toInt()

            if(value.isBlank() || input.size - 1 == index){
                sums.add(sum)

                sum = 0
            }
        }

        return sums
    }

    fun part1(input: List<String>): Int {
        return sums(input).max()
    }

    fun part2(input: List<String>): Int {
        return sums(input).sorted().takeLast(3).sum()
    }

    test(
        day = 1,
        testTarget1 = 24000,
        testTarget2 = 45000,
        part1 = ::part1,
        part2 = ::part2
    )
}
