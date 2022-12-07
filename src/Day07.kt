fun main() {
    abstract class FileSystem {
        abstract fun size(): Int
    }

    data class Directory(val path: String, val parent: Directory? = null) : FileSystem() {
        val files = mutableMapOf<String, FileSystem>()
        override fun size() = files.values.sumOf { it.size() }
    }

    data class File(val size: Int) : FileSystem() {
        override fun size() = size
    }

    fun parseFileSystem(input: List<String>): Map<String, Directory> {
        val directoryMap = mutableMapOf("/" to Directory("/"))
        var currentDirectory = directoryMap.values.first()

        for (cmd in input) {
            if (cmd.startsWith("\$ cd")) {
                val where = cmd.split("cd ").last().trim()

                currentDirectory = when (where) {
                    "/" -> directoryMap["/"]!!
                    ".." -> currentDirectory.parent!!
                    else -> directoryMap.getOrPut(currentDirectory.path + where + "/") {
                        Directory(currentDirectory.path + where + "/", currentDirectory)
                    }
                }
            } else if (cmd.startsWith("\$ ls")) {
//                println("ls")
            } else if (cmd.startsWith("dir")) {
                val (_, name) = cmd.split(" ")

                currentDirectory.files.putIfAbsent(name, directoryMap.getOrPut(currentDirectory.path + name + "/") {
                    Directory(currentDirectory.path + name + "/", currentDirectory)
                })
            } else if (cmd.first().isDigit()) {
                val (size, file) = cmd.split(" ")

                currentDirectory.files.putIfAbsent(file, File(size.toInt()))
            } else
                throw Exception("Unknown command $cmd")
        }

        return directoryMap
    }

    fun part1(input: List<String>) = parseFileSystem(input).values
        .map { it.size() }
        .filter { it <= 100000 }
        .sum()

    fun part2(input: List<String>): Int {
        val fs = parseFileSystem(input)

        val totalSize = 70000000
        val required = 30000000
        val currentUsed = fs["/"]!!.size()
        val currentFree = totalSize - currentUsed
        val missing = required - currentFree

        return fs.values
            .map { it.size() }
            .filter { it >= missing }
            .min()
    }

    test(
        day = 7,
        testTarget1 = 95437,
        testTarget2 = 24933642,
        part1 = ::part1,
        part2 = ::part2,
    )
}
