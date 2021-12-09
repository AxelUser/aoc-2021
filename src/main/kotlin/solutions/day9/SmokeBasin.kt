package solutions.day9

import solutions.BaseSolution

class SmokeBasin: BaseSolution() {
    override fun part1(input: String): String {
        val matrix = input.parseMatrix()
        val found = mutableListOf<Int>()

        val length = matrix.count()
        for (i in 0 until length) {
            val width = matrix[i].count()
            for (j in 0 until width) {
                if (matrix.getAdjacent(i to j).map { (ix, jx) -> matrix[ix][jx] }.all { it > matrix[i][j] }) {
                    found.add(matrix[i][j])
                }
            }
        }

        return found.sumOf { i -> i + 1 }.toString()
    }

    override fun part2(input: String): String {
        val matrix = input.parseMatrix()
        val visited = mutableSetOf<Pair<Int, Int>>()
        val basinSizes = mutableListOf<Int>()

        for ((i) in matrix.withIndex()) {
            for ((j) in matrix[i].withIndex()) {
                if(visited.contains(i to j) || matrix[i][j] == 9) continue
                basinSizes.add(bfs(i, j, matrix, visited))
            }
        }
        basinSizes.sortDescending()

        return basinSizes.take(3).reduce{ r, c -> r * c}.toString()
    }

    private fun bfs(i: Int, j: Int, matrix: Array<IntArray>, visited: MutableSet<Pair<Int, Int>>): Int {
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(i to j)
        var size = 0

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            if (!visited.contains(cur) && matrix[cur.first][cur.second] != 9) {
                size++
                visited.add(cur)
                queue.addAll(matrix.getAdjacent(cur))
            }
        }

        return size
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun String.parseMatrix(): Array<IntArray> {
        return this.lines().map { l -> l.map { c -> c.digitToInt() }.toIntArray() }.toTypedArray()
    }

    private fun Array<IntArray>.getAdjacent(point: Pair<Int, Int>): Sequence<Pair<Int, Int>> {
        val (i, j) = point
        val length = this@getAdjacent.count()
        val width = this@getAdjacent[i].count()
        return sequence {
            if (i > 0) yield(Pair(i - 1, j)) // top
            if (i < (length - 1)) yield(Pair(i + 1, j)) // bottom
            if (j > 0) yield(Pair(i, j - 1)) // left
            if (j < (width - 1)) yield(Pair(i, j + 1)) // right
        }
    }
}

fun main() {
    SmokeBasin().runAll()
}