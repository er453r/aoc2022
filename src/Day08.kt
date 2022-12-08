fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map { char -> char.digitToInt() } }

        val width = grid.first().size
        val height = grid.size

        val outside = 2 * width + 2 * (height - 2)
        var otherVisible = 0

        for(x in 1 until width - 1)
            for(y in 1 until height - 1){
                var visible = false
                val value = grid[x][y]

                for(nx in x + 1 until width) {
                    if (grid[nx][y] >= value)
                        break

                    if(nx == width - 1){
                        visible = true
                        break
                    }
                }

                if(!visible){
                    for(nx in x - 1 downTo 0) {
                        if (grid[nx][y] >= value)
                            break

                        if(nx == 0){
                            visible = true
                            break
                        }
                    }
                }

                if(!visible){
                    for(ny in y + 1 until height) {
                        if (grid[x][ny] >= value)
                            break

                        if(ny == height - 1){
                            visible = true
                            break
                        }
                    }
                }

                if(!visible){
                    for(ny in y - 1 downTo 0){
                        if (grid[x][ny] >= value)
                            break

                        if(ny == 0){
                            visible = true
                            break
                        }
                    }
                }

                if(visible)
                    otherVisible += 1
            }

//        println("outside $outside")
//        println("inside $otherVisible")

        return outside + otherVisible
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map { char -> char.digitToInt() } }

        val width = grid.first().size
        val height = grid.size

        var bestScore = 0

        for(x in 1 until width - 1)
            for(y in 1 until height - 1){
                val value = grid[y][x]

                var rightScore = 1
                var leftScore = 1
                var upScore = 1
                var downScore = 1

                for(nx in x + 1 until width) {
                    if (grid[y][nx] >= value)
                        break

                    if(nx != width - 1)
                        rightScore += 1
                }

                for(nx in x - 1 downTo 0) {
                    if (grid[y][nx] >= value)
                        break

                    if(nx != 0)
                        leftScore += 1
                }

                for(ny in y + 1 until height) {
                    if (grid[ny][x] >= value)
                        break

                    if(ny != height - 1)
                        upScore += 1
                }

                for(ny in y - 1 downTo 0){
                    if (grid[ny][x] >= value)
                        break

                    if(ny != 0)
                        downScore += 1
                }

                val score = upScore * downScore * leftScore * rightScore

                println("x $x y $y value $value score $score")

                if(score > bestScore)
                    bestScore = score
            }

        return bestScore
    }

    test(
        day = 8,
        testTarget1 = 21,
        testTarget2 = 8,
        part1 = ::part1,
        part2 = ::part2,
    )
}
