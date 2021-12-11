package solutions.day11

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
        queue.addAll(this.getAdjacent8(i to j))

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            if (!flashed.contains(cur)) {
                this[cur.first][cur.second]++
                if (this[cur.first][cur.second] > 9) {
                    flashed.add(cur)
                    queue.addAll(this.getAdjacent8(cur))
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

    @OptIn(ExperimentalStdlibApi::class)
    private fun String.parseMatrix(): Array<IntArray> {
        return this.lines().map { l -> l.map { c -> c.digitToInt() }.toIntArray() }.toTypedArray()
    }

    private fun Array<IntArray>.getAdjacent8(point: Pair<Int, Int>): Sequence<Pair<Int, Int>> {
        val (i, j) = point
        val length = this@getAdjacent8.count()
        val width = this@getAdjacent8[i].count()
        return sequence {
            if (i > 0) {
                yield(Pair(i - 1, j)) // top
                if (j > 0) yield(Pair(i - 1, j - 1)) // top-left
                if (j < (width - 1)) yield(Pair(i - 1, j + 1)) // top-right
            }
            if (i < (length - 1)) {
                yield(Pair(i + 1, j)) // bottom
                if (j > 0) yield(Pair(i + 1, j - 1)) // bottom-left
                if (j < (width - 1)) yield(Pair(i + 1, j + 1)) // bottom-right
            }

            if (j > 0) yield(Pair(i, j - 1)) // left
            if (j < (width - 1)) yield(Pair(i, j + 1)) // right
        }
    }
}

fun main() {
    DumboOctopus().runAll()
}