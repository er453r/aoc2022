fun main() {
    fun part1(input: List<String>): Int {
        var score = 0

        for (line in input) {
            val opponent = line.split(" ").first()
            val me = line.split(" ").last()

            score += when (opponent) {
                "A" -> when (me) {
                    "X" -> 1 + 3
                    "Y" -> 2 + 6
                    "Z" -> 3 + 0
                    else -> throw Exception("impossible")
                }

                "B" -> when (me) {
                    "X" -> 1 + 0
                    "Y" -> 2 + 3
                    "Z" -> 3 + 6
                    else -> throw Exception("impossible")
                }

                "C" -> when (me) {
                    "X" -> 1 + 6
                    "Y" -> 2 + 0
                    "Z" -> 3 + 3
                    else -> throw Exception("impossible")
                }

                else -> throw Exception("impossible")
            }
        }

        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0

        for (line in input) {
            val opponent = line.split(" ").first()
            val result = line.split(" ").last()

            score += when (opponent) {
                "A" -> when (result) {
                    "X" -> 0 + 3
                    "Y" -> 3 + 1
                    "Z" -> 6 + 2
                    else -> throw Exception("impossible")
                }

                "B" -> when (result) {
                    "X" -> 0 + 1
                    "Y" -> 3 + 2
                    "Z" -> 6 + 3
                    else -> throw Exception("impossible")
                }

                "C" -> when (result) {
                    "X" -> 0 + 2
                    "Y" -> 3 + 3
                    "Z" -> 6 + 1
                    else -> throw Exception("impossible")
                }

                else -> throw Exception("impossible")
            }
        }

        return score
    }

    test(
        day = 2,
        testTarget1 = 15,
        testTarget2 = 12,
        part1 = ::part1,
        part2 = ::part2
    )
}
