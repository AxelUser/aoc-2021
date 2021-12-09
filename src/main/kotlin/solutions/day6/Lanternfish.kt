package solutions.day6

import solutions.BaseSolution

class Lanternfish: BaseSolution() {
    override fun part1(input: String): Any {
        return solve(input, 80)
    }

    override fun part2(input: String): Any {
        return solve(input, 256)
    }

    private fun solve(input: String, days: Int): Long {
        val states = "\\d+".toRegex().findAll(input)
            .map { it.value.toInt() }
            .fold(LongArray(9){0}){ s, n -> s[n]++; s }

        for (t in 0 until days) {
            val newborn = states[0]

            for (i in 1 until 9) {
                states[i - 1] = states[i]
            }

            states[8] = newborn
            states[6] += newborn
        }

        return states.sum()
    }
}

fun main() {
    Lanternfish().runAll()
}