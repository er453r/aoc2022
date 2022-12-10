fun main() {
    fun drawPixel(cycle:Int, x:Int, width:Int){
//        println("$cycle $x ${cycle % 40} in ${(x .. x+3)} = ${cycle % 40 in (x .. x+3)}")

        if(cycle % 40 in (x until x+3))
            print("#")
        else
            print(" ")

        if(cycle % 5 == 0)
            print("    ")

        if(cycle % 40 == 0)
            println()
    }

    fun part1(input: List<String>): Int {
        var currentCycle = 0
        var x = 1

        var targetCycles = arrayOf(20, 60, 100, 140, 180, 220)
        var mapOfTargets = mutableMapOf<Int, Int>()

        val width = 40

        println()

        for(line in input){
            currentCycle += 1 // start fo cycle

            if(currentCycle in targetCycles)
                mapOfTargets[currentCycle] = x

            drawPixel(currentCycle, x, width)

            if(line == "noop"){
                // do nothing
            }
            else if(line.startsWith("addx")){
                currentCycle += 1 // end cycle, start new

                if(currentCycle in targetCycles)
                    mapOfTargets[currentCycle] = x

                drawPixel(currentCycle, x, width)

                x += line.split(" ").last().toInt()
            }
            else
                throw Exception("Unknown command $line")
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
