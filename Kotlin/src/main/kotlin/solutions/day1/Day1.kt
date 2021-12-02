package solutions.day1

import solutions.Solution

class Day1 : Solution() {
    override val inputDir: String = "day1"

    override fun part1(input: String): String {
        return input.lines()
            .map { it.toInt() }
            .countIncreased(1).toString()
    }

    override fun part2(input: String): String {
        return input.lines()
            .map { it.toInt() }
            .countIncreased(3).toString()
    }

    private fun Iterable<Int>.countIncreased(windowSize: Int): Int {
        return this.windowed(windowSize + 1).count {it.first() < it.last()}
    }
}