package solutions.day7

import solutions.BaseSolution
import kotlin.math.abs
import kotlin.math.floor

class TreacheryOfWhales: BaseSolution() {
    override fun part1(input: String): String {
        val crabs = input.split(',').map { it.toInt() }.toList().sorted()
        val target = crabs[crabs.count() / 2]
        val sum = crabs.sumOf { abs(target - it) }
        return sum.toString()
    }

    override fun part2(input: String): String {
        val crabs = input.split(',').map { it.toDouble() }.toList()
        val target1 = floor(crabs.sum() / crabs.count()).toInt()
        val target2 = target1 + 1
        val arithmeticProg = {n: Double -> (n / 2) * (2 + (n - 1))}
        val sum = arrayOf(target1, target2).minOf { t -> crabs.sumOf { arithmeticProg(abs(t - it)) } }
        return sum.toBigDecimal().toPlainString()
    }
}

fun main() {
    TreacheryOfWhales().runAll()
}