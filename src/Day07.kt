fun main() {
    abstract class FileSystem{
        abstract fun size():Int
    }
    data class Directory(val path:String, val parent:Directory? = null): FileSystem() {
        val files = mutableMapOf<String, FileSystem>()
        override fun size() = files.values.sumOf { it.size() }
    }

    data class File(val size:Int): FileSystem() {
        override fun size() = size
    }

    fun part1(input: List<String>): Int {
        val directoryMap = mutableMapOf<String, Directory>()

        val root = Directory("/")
        directoryMap[root.path] = root

        var currentDirectory = root

        for(cmd in input){
            if(cmd.startsWith("\$ cd")){
                val where = cmd.split("cd ").last().trim()

                println("changing dir ${currentDirectory.path} to $where")

                currentDirectory = when (where) {
                    "/" -> root
                    ".." -> currentDirectory.parent!!
                    else -> directoryMap.getOrPut(currentDirectory.path + where + "/"){
                        Directory(currentDirectory.path + where + "/", currentDirectory)
                    }
                }
            }
            else if(cmd.startsWith("\$ ls")){
                println("ls")
            }
            else if(cmd.startsWith("dir")){
                val (type, name) = cmd.split(" ")

                println("$type named $name")

                currentDirectory.files.putIfAbsent(name, directoryMap.getOrPut(currentDirectory.path + name + "/"){
                    Directory(currentDirectory.path + name + "/", currentDirectory)
                })
            }
            else if(cmd.first().isDigit()){
                val (size, file) = cmd.split(" ")

                println("file $file of size $size")

                currentDirectory.files.putIfAbsent(file, File(size.toInt()))
            }
            else
                throw Exception("Unknown command $cmd")
        }

        return directoryMap.values.map {
            println("${it.path} - ${it.size()} - ${it.files.keys}")
            it.size()
        }.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val directoryMap = mutableMapOf<String, Directory>()

        val root = Directory("/")
        directoryMap[root.path] = root

        var currentDirectory = root

        for(cmd in input){
            if(cmd.startsWith("\$ cd")){
                val where = cmd.split("cd ").last().trim()

                println("changing dir ${currentDirectory.path} to $where")

                currentDirectory = when (where) {
                    "/" -> root
                    ".." -> currentDirectory.parent!!
                    else -> directoryMap.getOrPut(currentDirectory.path + where + "/"){
                        Directory(currentDirectory.path + where + "/", currentDirectory)
                    }
                }
            }
            else if(cmd.startsWith("\$ ls")){
                println("ls")
            }
            else if(cmd.startsWith("dir")){
                val (type, name) = cmd.split(" ")

                println("$type named $name")

                currentDirectory.files.putIfAbsent(name, directoryMap.getOrPut(currentDirectory.path + name + "/"){
                    Directory(currentDirectory.path + name + "/", currentDirectory)
                })
            }
            else if(cmd.first().isDigit()){
                val (size, file) = cmd.split(" ")

                println("file $file of size $size")

                currentDirectory.files.putIfAbsent(file, File(size.toInt()))
            }
            else
                throw Exception("Unknown command $cmd")
        }

        var totalSize = 70000000
        val required = 30000000
        val currentUsed = root.size()
        val currentFree = totalSize - currentUsed
        val missing = required - currentFree

        println("totalSize $totalSize")
        println("required $required")
        println("currentUsed $currentUsed")
        println("currentFree $currentFree")
        println("missing $missing")

        return directoryMap.values.map {
            it.size()
        }.filter { it >= missing }.min()
    }

    test(
        day = 7,
        testTarget1 = 95437,
        testTarget2 = 24933642,
        part1 = ::part1,
        part2 = ::part2,
    )
}
