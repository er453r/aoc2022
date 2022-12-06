fun main() {
    fun findUniqueSequence(haystack:String, sequenceLength:Int):Int{
        val queue = ArrayDeque<Char>()

        for(i in haystack.indices){
            queue.addLast(haystack[i])

            if(queue.size > sequenceLength)
                queue.removeFirst()

            if(queue.toSet().size == sequenceLength)
                return i + 1
        }

        return -1
    }

    fun part1(input: List<String>) = findUniqueSequence(input.first(), 4)

    fun part2(input: List<String>) = findUniqueSequence(input.first(), 14)

    test(
        day = 6,
        testTarget1 = 7,
        testTarget2 = 19,
        part1 = ::part1,
        part2 = ::part2,
    )
}
