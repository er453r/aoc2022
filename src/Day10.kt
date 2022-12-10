fun main() {
    fun part1(input: List<String>): Int {
        var currentCycle = 0
        var x = 1
        var sum = 0

        fun cycle(){
            sum += if(++currentCycle % 40 == 20) x * currentCycle else 0

            print(if((currentCycle - 1) % 40 + 1 in (x until x+3)) "#" else " ")

            if(currentCycle % 40 == 0)
                println()
        }

        println()

        for(line in input){
            cycle()

            if(line.startsWith("addx")){
                cycle()

                x += line.substringAfter(" ").toInt()
            }
        }

        return sum
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
