import kotlin.math.sign

fun main() {
    data class ListOrValue(val value:Int? = null, val list:List<ListOrValue>? = null)

    fun parseLine(line:String):ListOrValue{
        if(line.startsWith("[")){ // find the end
            val list = mutableListOf<ListOrValue>()

            var stackSize = 0
            var startPosition = 1

            for(n in 1 until line.length) {
                if (line[n] == '[')
                    stackSize++

                if (line[n] == ']')
                    stackSize--

                if ((((line[n] == ',') && stackSize == 0) || n == line.length - 1) && startPosition != n) {
                    list.add(parseLine(line.substring(startPosition, n)))

                    startPosition = n + 1
                }
            }

            return ListOrValue(list = list)
        }
        else
            return ListOrValue(value = line.toInt())
    }

    fun compare(left:ListOrValue, right:ListOrValue):Int{
        if(left.value != null && right.value != null) // both are ints
            return left.value.compareTo(right.value).sign // negative if less
        else if(left.list != null && right.list != null){
            for(n in 0 until left.list.size){
                if(right.list.size < n + 1)
                    return 1

                when(val itemComparison = compare(left.list[n], right.list[n])){
                    0 -> continue
                    else -> return itemComparison
                }
            }

            if(left.list.size < right.list.size)
                return -1

            return 0
        }
        else{
            val leftConverted = if(left.value != null) ListOrValue(list = listOf(left)) else left
            val rightConverted = if(right.value != null) ListOrValue(list = listOf(right)) else right

            return compare(leftConverted, rightConverted)
        }
    }

    fun compareStrings(left:String, right:String) = compare(parseLine(left), parseLine(right))

    fun part1(input: List<String>): Int {
        val correctPairsIndicies = mutableListOf<Int>()

        input.chunked(3).forEachIndexed { index, strings ->
            val left = parseLine(strings[0])
            val right = parseLine(strings[1])

            if(compare(left, right) < 0)
                correctPairsIndicies.add(index + 1)
        }

        return correctPairsIndicies.sum()
    }

    fun part2(input: List<String>): Int {
        val withDividers = input.toMutableList()
        withDividers.add("[[2]]")
        withDividers.add("[[6]]")

        val sorted = withDividers.filter { it.isNotBlank() }
            .sortedWith(::compareStrings)

        var product = 1

        sorted.forEachIndexed { index, line ->
            println(line)

            if(line == "[[2]]" || line == "[[6]]")
                product *= index + 1
        }

        return product
    }

    test(
        day = 13,
        testTarget1 = 13,
        testTarget2 = 140,
        part1 = ::part1,
        part2 = ::part2,
    )
}
