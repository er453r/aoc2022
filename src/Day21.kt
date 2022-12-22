fun main() {
    class Monkey(val name: String, var value: Long?, val operation: String) {
        fun yell(monkeys: Map<String, Monkey>): Long = if (value != null)
            value!!
        else {
            val (name1, operator, name2) = operation.split(" ")
            val monkey1 = monkeys[name1]!!.yell(monkeys)
            val monkey2 = monkeys[name2]!!.yell(monkeys)

            when (operator) {
                "+" -> monkey1 + monkey2
                "-" -> monkey1 - monkey2
                "*" -> monkey1 * monkey2
                "/" -> monkey1 / monkey2
                else -> throw Exception("Unknown operator $operator")
            }
        }

        fun dependsOn(name: String, monkeys: Map<String, Monkey>): Boolean {
            if (this.name == name)
                return true
            else if (this.value != null)
                return false
            else {
                val (name1, _, name2) = operation.split(" ")

                if (name1 == name || name2 == name)
                    return true

                val monkey1 = monkeys[name1]!!.dependsOn(name, monkeys)
                val monkey2 = monkeys[name2]!!.dependsOn(name, monkeys)

                return monkey1 || monkey2
            }
        }

        fun fix(targetValue: Long, nodeToFix: String, monkeys: Map<String, Monkey>) {
            if (this.name == nodeToFix) {
                this.value = targetValue

                return
            } else if (this.value != null)
                throw Exception("Should not be fixing here!")
            else {
                val (name1, operator, name2) = operation.split(" ")

                val monkey1 = monkeys[name1]!!
                val monkey2 = monkeys[name2]!!
                val monkey1dependOnNodeToFix = monkey1.dependsOn(nodeToFix, monkeys)
                val otherValue = if (monkey1dependOnNodeToFix) monkey2.yell(monkeys) else monkey1.yell(monkeys)

                if (monkey1dependOnNodeToFix) {
                    when (operator) {
                        "+" -> monkey1.fix(targetValue - otherValue, nodeToFix, monkeys)
                        "-" -> monkey1.fix(targetValue + otherValue, nodeToFix, monkeys)
                        "*" -> monkey1.fix(targetValue / otherValue, nodeToFix, monkeys)
                        "/" -> monkey1.fix(targetValue * otherValue, nodeToFix, monkeys)
                        else -> throw Exception("Unknown operator $operator")
                    }
                } else {
                    when (operator) {
                        "+" -> monkey2.fix(targetValue - otherValue, nodeToFix, monkeys)
                        "-" -> monkey2.fix(otherValue - targetValue, nodeToFix, monkeys)
                        "*" -> monkey2.fix(targetValue / otherValue, nodeToFix, monkeys)
                        "/" -> monkey2.fix(otherValue / targetValue, nodeToFix, monkeys)
                        else -> throw Exception("Unknown operation $operator")
                    }
                }

            }
        }
    }

    fun parseMonkeys(input: List<String>) = input.map { it.split(": ") }.associate { (name, yell) ->
        name to Monkey(name, value = yell.toLongOrNull(), operation = yell)
    }

    fun part1(input: List<String>) = parseMonkeys(input).let { it["root"]!!.yell(it) }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input)

        val humn = monkeys["humn"]!!
        val root = monkeys["root"]!!
        val (root1name, _, root2name) = root.operation.split(" ")
        val root1 = monkeys[root1name]!!
        val root2 = monkeys[root2name]!!
        val root1dependOnHumn = root1.dependsOn("humn", monkeys)
        val targetValue = if (root1dependOnHumn) root2.yell(monkeys) else root1.yell(monkeys)
        val branchToFix = if (root1dependOnHumn) root1 else root2

        branchToFix.fix(targetValue, "humn", monkeys)

        return humn.value!!
    }

    test(
        day = 21,
        testTarget1 = 152L,
        testTarget2 = 301L,
        part1 = ::part1,
        part2 = ::part2,
    )
}
