fun main() {
    class Node(val rate: Int, var paths: List<Node> = emptyList())

    val inputLineRegex = """Valve ([A-Z]+) has flow rate=(\d+)""".toRegex()
    val input2LineRegex = """([A-Z]+)""".toRegex()

    fun parseData(input: List<String>): Map<String, Node> {
        val nodes = mutableMapOf<String, Node>()

        for (line in input)
            line.split(";").first().destructured(inputLineRegex).let { (name, rate) -> nodes[name] = Node(rate.toInt()) }

        for (line in input) {
            val (name) = line.split(";").first().destructured(inputLineRegex)
            val paths = input2LineRegex.findAll(line.split(";").last()).map { nodes[it.value]!! }.toList()

            nodes[name]!!.paths = paths
        }

        return nodes
    }

    fun bestPath(startNode: Node, targetNodesLeft: Set<Node>, minutes: Int): Pair<Int, List<Node>> {
        fun visitNode(node: Node, minutesLeft: Int, pressureReleased: Int, opened: Set<Node>, closed: Set<Node>, visited: List<Node>): Pair<Int, List<Node>> {
            val dp = opened.sumOf { it.rate }

            val reachableClosed = closed.map { Pair(it, aStar(node, {end -> end == it}, { 0 }, { n -> n.paths })) }
                .filter { (_, steps) -> steps.size <= minutesLeft }

            if (reachableClosed.isEmpty() || minutesLeft == 0)
                return Pair(pressureReleased + (minutesLeft - 1) * dp, visited)

            return reachableClosed.map { (candidate, path) ->
                visitNode(candidate, minutesLeft - path.size, pressureReleased + path.size * dp + candidate.rate, opened + candidate, closed - candidate, visited + candidate)
            }.maxBy { it.first }
        }

        return visitNode(startNode, minutes, 0, emptySet(), targetNodesLeft, listOf())
    }

    fun part1(input: List<String>): Int {
        val nodes = parseData(input)
        val nonZeroClosedNodes = nodes.values.filter { it.rate != 0 }.toSet()
        val bestPath = bestPath(nodes["AA"]!!, nonZeroClosedNodes, 30)

        return bestPath.first
    }

    fun part2(input: List<String>): Int {
        val nodes = parseData(input)
        val nonZeroClosedNodes = nodes.values.filter { it.rate != 0 }.toSet()
        val myPath = bestPath(nodes["AA"]!!, nonZeroClosedNodes, 26)
        val elephantPath = bestPath(nodes["AA"]!!, nonZeroClosedNodes - myPath.second.toSet(), 26)

        return myPath.first + elephantPath.first
    }

    test(
        day = 16,
        testTarget1 = 1651,
        testTarget2 = 1327, // should be 1707??
        part1 = ::part1,
        part2 = ::part2,
    )
}
