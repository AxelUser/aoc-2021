package solutions.day1

import solutions.BaseSolution

class SonarSweep : BaseSolution() {
    override fun part1(input: String): Any {
        return input.lines()
            .map { it.toInt() }
            .countIncreased(1)
    }

    override fun part2(input: String): Any {
        return input.lines()
            .map { it.toInt() }
            .countIncreased(3)
    }

    private fun Iterable<Int>.countIncreased(windowSize: Int): Int {
        return this.windowed(windowSize + 1).count {it.first() < it.last()}
    }
}

fun main() {
    SonarSweep().runAll()
}