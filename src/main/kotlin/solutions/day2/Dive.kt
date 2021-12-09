package solutions.day2

import solutions.BaseSolution

class Dive : BaseSolution() {
    override fun part1(input: String): Any {
        val ops = input.lines()
            .map { it.split(' ')}
            .map { Pair(it[0], it[1].toInt())}
            .groupingBy { it.first }
            .foldTo(mutableMapOf(), 0) { acc, (_, n) -> acc + n }

        return ops["forward"]!! * (ops["down"]!! - ops["up"]!!)
    }

    override fun part2(input: String): Any {
        val (pos, _, depth) = input.lines()
            .map { it.split(' ')}
            .map { Pair(it[0], it[1].toInt())}
            .fold(Triple(0, 0, 0)) { (pos ,aim, depth), (op, v) ->
                when (op) {
                    "forward" -> Triple(pos + v, aim, depth + aim * v)
                    "down" -> Triple(pos, aim + v, depth)
                    "up" -> Triple(pos, aim - v, depth)
                    else -> Triple(pos, aim, depth)
                }
            }

        return pos * depth
    }
}

fun main() {
    Dive().runAll()
}