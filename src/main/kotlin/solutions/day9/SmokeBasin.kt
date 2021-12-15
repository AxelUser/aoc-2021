package solutions.day9

import common.getAdjacent4
import common.parseMatrix
import solutions.BaseSolution

class SmokeBasin: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parseMatrix().let { matrix ->
            mutableListOf<Int>().apply {
                for ((i) in matrix.withIndex()) {
                    for ((j) in matrix[i].withIndex()) {
                        if (matrix.getAdjacent4(i to j).map { (ix, jx) -> matrix[ix][jx] }.all { it > matrix[i][j] }) {
                            add(matrix[i][j])
                        }
                    }
                }
            }.sumOf { i -> i + 1 }
        }
    }

    override fun part2(input: String): Any {
        return input.parseMatrix().let { matrix ->
            val visited = mutableSetOf<Pair<Int, Int>>()
            mutableListOf<Int>().apply {
                for ((i) in matrix.withIndex()) {
                    for ((j) in matrix[i].withIndex()) {
                        if(visited.contains(i to j) || matrix[i][j] == 9) continue
                        add(bfs(i, j, matrix, visited))
                    }
                }
            } }
            .apply { sortDescending() }
            .take(3).reduce{ r, c -> r * c}
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
                queue.addAll(matrix.getAdjacent4(cur))
            }
        }

        return size
    }
}

fun main() {
    SmokeBasin().runAll()
}