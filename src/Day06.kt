fun main() {
    fun part1(input: List<String>): Int {
        val queue = ArrayDeque<Char>()
        val haystack = input.first()

        for(i in 0 until haystack.length){
            queue.addLast(haystack[i])

            if(queue.size > 4)
                queue.removeFirst()

            if(queue.toSet().size == 4)
                return i + 1
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val queue = ArrayDeque<Char>()
        val haystack = input.first()

        for(i in 0 until haystack.length){
            queue.addLast(haystack[i])

            if(queue.size > 14)
                queue.removeFirst()

            if(queue.toSet().size == 14)
                return i + 1
        }

        return 0
    }

    test(
        day = 6,
        testTarget1 = 7,
        testTarget2 = 19,
        part1 = ::part1,
        part2 = ::part2,
    )
}
