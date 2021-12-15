package solutions.day11

import common.getAdjacent8
import common.parseMatrix
import solutions.BaseSolution

class DumboOctopus: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parseMatrix().runSteps().take(100).sum()
    }

    override fun part2(input: String): Any {
        val octopuses = input.parseMatrix()
        val total = octopuses.sumOf { it.count() }
        return octopuses.runSteps().withIndex().first { (_, flashed) -> flashed == total }.index + 1
    }

    private fun Array<IntArray>.runSteps(): Sequence<Int> {
        val m = this
        return sequence {
            while (true) {
                val flashes = m.run {
                    val initialFlashedOnStep = incrementAll().toSet()
                    val flashedOnStep = initialFlashedOnStep.toMutableSet()
                    initialFlashedOnStep.forEach{ (i, j) -> propagateFlash(i, j, flashedOnStep) }
                    m.resetFlashed(flashedOnStep)
                    flashedOnStep.count()
                }

                yield(flashes)
            }
        }
    }

    private fun Array<IntArray>.propagateFlash(i: Int, j: Int, flashed: MutableSet<Pair<Int, Int>>) {
        val queue = ArrayDeque<Pair<Int, Int>>()
        // adding adjacent of current flashed octopus
        queue.addAll(getAdjacent8(i to j))

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            if (!flashed.contains(cur)) {
                this[cur.first][cur.second]++
                if (this[cur.first][cur.second] > 9) {
                    flashed.add(cur)
                    queue.addAll(getAdjacent8(cur))
                }
            }
        }
    }

    private fun Array<IntArray>.incrementAll(): Sequence<Pair<Int, Int>> {
        return sequence {
            for ((i) in this@incrementAll.withIndex()) {
                for ((j) in this@incrementAll[i].withIndex()) {
                    this@incrementAll[i][j]++
                    if (this@incrementAll[i][j] > 9) {
                        yield(Pair(i, j))
                    }
                }
            }
        }
    }

    private fun Array<IntArray>.resetFlashed(flashed: Set<Pair<Int, Int>>) {
        flashed.forEach { (i, j) -> this[i][j] = 0 }
    }
}

fun main() {
    DumboOctopus().runAll()
}