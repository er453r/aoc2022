fun main() {
    fun drawPixel(cycle:Int, x:Int){
        print(if((cycle - 1) % 40 + 1 in (x until x+3)) "#" else " ")

        if(cycle % 40 == 0)
            println()
    }

    fun cycle(x:Int, currentCycle:Int, targetCycles:Array<Int>, mapOfTargets:MutableMap<Int, Int>){
        if(currentCycle in targetCycles)
            mapOfTargets[currentCycle] = x

        drawPixel(currentCycle, x)
    }

    fun part1(input: List<String>): Int {
        var currentCycle = 0
        var x = 1

        val targetCycles = arrayOf(20, 60, 100, 140, 180, 220)
        val mapOfTargets = mutableMapOf<Int, Int>()

        println()

        for(line in input){
            cycle(x, ++currentCycle, targetCycles, mapOfTargets)

            if(line.startsWith("addx")){
                cycle(x, ++currentCycle, targetCycles, mapOfTargets)

                x += line.split(" ").last().toInt()
            }
        }

        return mapOfTargets.entries.sumOf { it.key * it.value }
    }

    fun part2(input: List<String>) = part1(input)

    test(
        day = 10,
        testTarget1 = 13140,
        testTarget2 = 13140,
        part1 = ::part1,
        part2 = ::part2,
    )
}
