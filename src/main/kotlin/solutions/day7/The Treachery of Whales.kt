package solutions.day7

import solutions.BaseSolution
import kotlin.math.abs
import kotlin.math.floor

class `The Treachery of Whales`: BaseSolution() {
    override fun part1(input: String): String {
        val crabs = input.split(',').map { it.toInt() }.toList().sorted()
        // input length is always even
        val target = crabs[crabs.count() / 2]
        val sum = crabs.sumOf { abs(target - it) }
        return sum.toString()
    }

    override fun part2(input: String): String {
        val crabs = input.split(',').map { it.toDouble() }.toList()
        val target = floor(crabs.sum() / crabs.count())
        val arithmeticProg = {n: Double -> (n / 2) * (2 + (n - 1))}
        val sum = crabs.sumOf { arithmeticProg(abs(target - it)) }
        return sum.toBigDecimal().toPlainString()
    }
}