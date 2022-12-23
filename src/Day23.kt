fun main() {
    val directionsToConsider = arrayOf(
        arrayOf(
            Vector2d.UP,
            Vector2d.UP + Vector2d.LEFT,
            Vector2d.UP + Vector2d.RIGHT,
        ),
        arrayOf(
            Vector2d.DOWN,
            Vector2d.DOWN + Vector2d.LEFT,
            Vector2d.DOWN + Vector2d.RIGHT,
        ),
        arrayOf(
            Vector2d.LEFT,
            Vector2d.LEFT + Vector2d.UP,
            Vector2d.LEFT + Vector2d.DOWN,
        ),
        arrayOf(
            Vector2d.RIGHT,
            Vector2d.RIGHT + Vector2d.UP,
            Vector2d.RIGHT + Vector2d.DOWN,
        ),
    )

    fun diffuseElves(elves: MutableSet<Vector2d>, stopAfterRound: Int = -1): Int {
        var rounds = 1
        var directionsToConsiderIndex = 0

        while (true) {
            val moveProposals = mutableMapOf<Vector2d, Vector2d>()
            val moveProposalsDuplicates = mutableSetOf<Vector2d>()
            val moveProposalsElves = mutableSetOf<Vector2d>()

            // first half
            for (elf in elves) {
                val neighbours = elves.intersect(elf.neighbours8())

                if (neighbours.isEmpty())
                    continue

                for (n in directionsToConsider.indices) {
                    val directionToConsiderIndex = (directionsToConsiderIndex + n) % directionsToConsider.size
                    val directionNeighbours = directionsToConsider[directionToConsiderIndex].map { elf + it }.toSet()

                    if (directionNeighbours.intersect(neighbours).isEmpty()) {
                        val moveProposal = directionNeighbours.first()

                        if (moveProposal in moveProposalsDuplicates)
                            break

                        if (moveProposal in moveProposals) {
                            moveProposalsDuplicates += moveProposal
                            moveProposalsElves -= moveProposals[moveProposal]!!
                            moveProposals -= moveProposal

                            break
                        }

                        moveProposals[moveProposal] = elf
                        moveProposalsElves += elf

                        break
                    }
                }
            }

            if (stopAfterRound == -1 && moveProposals.isEmpty())
                break

            // second half
            elves -= moveProposalsElves
//            elves -= moveProposals.values.toSet()
            elves += moveProposals.keys

            directionsToConsiderIndex = (directionsToConsiderIndex + 1) % directionsToConsider.size

            if (rounds++ == stopAfterRound)
                break
        }

        return rounds
    }

    fun part1(input: List<String>): Int {
        val elves = Grid(input.map { line -> line.toCharArray().toList() }).data.flatten().filter { it.value == '#' }.map { it.position }.toMutableSet()

        diffuseElves(elves, stopAfterRound = 10)

        val minX = elves.minOf { it.x }
        val maxX = elves.maxOf { it.x }
        val minY = elves.minOf { it.y }
        val maxY = elves.maxOf { it.y }

        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    fun part2(input: List<String>): Int {
        val elves = Grid(input.map { line -> line.toCharArray().toList() }).data.flatten().filter { it.value == '#' }.map { it.position }.toMutableSet()

        return diffuseElves(elves)
    }

    test(
        day = 23,
        testTarget1 = 110,
        testTarget2 = 20,
        part1 = ::part1,
        part2 = ::part2,
    )
}
