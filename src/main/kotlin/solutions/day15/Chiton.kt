package solutions.day15

import common.getAdjacent4
import common.parseMatrix
import solutions.BaseSolution
import java.util.*

class Chiton: BaseSolution() {
    override fun part1(input: String): Any {
        return input.solve()
    }

    override fun part2(input: String): Any {
        return input.solve(5)
    }

    private fun String.solve(scale: Int = 1): Int {
        return parseMatrix().let { matrix ->
            dijkstra(Pair(0, 0), matrix.bottomRight(scale)){ point ->
                matrix.getAdjacent4(point, scale).map { adj -> State(adj, matrix.getRisk(adj)) }
            }
        }
    }

    private data class State(val point: Pair<Int, Int>, val risk: Int)

    private fun dijkstra(start: Pair<Int, Int>, destination: Pair<Int, Int>, getAdjacent: (Pair<Int, Int>) -> Sequence<State>): Int {
        val pq = PriorityQueue<State>(compareBy { it.risk })
        val visited = mutableSetOf<Pair<Int, Int>>()
        pq.offer(State(start, 0))

        while (pq.isNotEmpty()) {
            val safest = pq.remove()
            if (safest.point == destination) {
                return safest.risk
            }
            for (adj in getAdjacent(safest.point)) {
                if (!visited.contains(adj.point)) {
                    visited.add(adj.point)
                    pq.offer(State(adj.point, adj.risk + safest.risk))
                }
            }
        }

        error("not found")
    }

    private fun Array<IntArray>.getRisk(point: Pair<Int, Int>): Int {
        val (y, x) = point
        val length = this@getRisk.count()
        val width = this@getRisk[0].count()

        val increase = y / length + x / width
        return (((this[y % length][x % width] - 1) + increase) % 9) + 1
    }

    private fun Array<IntArray>.bottomRight(scale: Int = 1): Pair<Int, Int> {
        return Pair(count() * scale - 1, first().count() * scale - 1)
    }
}

fun main() {
    Chiton().runAll()
}