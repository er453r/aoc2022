import kotlin.math.absoluteValue

fun main() {
    class Wrapper(val value: Long)

    fun mix(originalOrder:List<Wrapper>, mixed:MutableList<Wrapper>){

//        println(mixed.map { it.value })

        for (entry in originalOrder) {
            val entryPositionInMixed = mixed.indexOf(entry)
            mixed.remove(entry)

            var newEntryPosition = entryPositionInMixed + entry.value

            if(newEntryPosition < 0){
                val n = (newEntryPosition.absoluteValue / mixed.size) + 1

                newEntryPosition += n * mixed.size
            }


            newEntryPosition %= mixed.size

            if(newEntryPosition == 0L)
                newEntryPosition = mixed.size.toLong()

//            println("Moving ${entry.value} between ${mixed[(newEntryPosition + mixed.size + - 1) % mixed.size].value} and ${mixed[(newEntryPosition) % mixed.size].value}")

            mixed.add(newEntryPosition.toInt(), entry)
//            println(mixed.map { it.value })
        }
    }

    fun part1(input: List<String>): Long {
        val originalOrder = input.map { Wrapper(it.toLong()) }
        val mixed = originalOrder.toMutableList()

        mix(originalOrder, mixed)

        val zeroPosition = mixed.indexOfFirst { it.value == 0L }

        return arrayOf(1000, 2000, 3000).map {
            mixed[(zeroPosition + it) % mixed.size].value
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val originalOrder = input.map { Wrapper(it.toLong() * 811589153) }
        val mixed = originalOrder.toMutableList()

        repeat(10){ mix(originalOrder, mixed) }

        val zeroPosition = mixed.indexOfFirst { it.value == 0L }

        return arrayOf(1000, 2000, 3000).map {
            mixed[(zeroPosition + it) % mixed.size].value
        }.sum()
    }

    test(
        day = 20,
        testTarget1 = 3,
        testTarget2 = 1623178306,
        part1 = ::part1,
        part2 = ::part2,
    )
}
