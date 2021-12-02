package solutions.day2

import solutions.Solution

class Day2 : Solution() {
    override val inputDir: String = "day2"

    override fun part1(input: String): String {
        val ops = input.lines()
            .map { it.split(' ')}
            .map { Pair(it[0], it[1].toInt())}
            .groupingBy { it.first }
            .foldTo(mutableMapOf() ,0) { acc, (_, n) -> acc + n }

        return (ops["forward"]!! * (ops["down"]!! - ops["up"]!!)).toString()
    }

    override fun part2(input: String): String {
        val (pos, _, depth) = input.lines()
            .map { it.split(' ')}
            .map { Pair(it[0], it[1].toInt())}
            .fold(Triple(0, 0, 0)) { (pos ,aim, depth), (op, v) ->
                when (op) {
                    "forward" -> {
                        Triple(pos + v, aim, depth + aim * v)
                    }
                    "down" -> {
                        Triple(pos, aim + v, depth)
                    }
                    "up" -> {
                        Triple(pos, aim - v, depth)
                    }
                    else -> Triple(pos, aim, depth)
                }
            }

        return (pos * depth).toString()
    }
}