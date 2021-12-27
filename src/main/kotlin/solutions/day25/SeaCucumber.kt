package solutions.day25

import solutions.BaseSolution

class SeaCucumber: BaseSolution() {
    override fun part1(input: String): Any {
        var (length, width, old) = input.parse()

        var count = 0
        while (true) {
            count++
            val new = old.afterStep(length, width)
            if (new == old) {
                return count
            }
            old = new
        }
    }

    override fun part2(input: String): Any {
        return "Happy holidays!"
    }

    private fun Map<Pair<Int, Int>, Char>.afterStep(length: Int, width: Int): Map<Pair<Int, Int>, Char> {
        val newMap = mutableMapOf<Pair<Int, Int>, Char>()

        filter { it.value == '>' }.forEach { (y, x), c ->
            val check = y to if (x == width - 1) 0 else x + 1
            if (containsKey(check)) {
                newMap[y to x] = c
            } else {
                newMap[check] = c
            }
        }

        filter { it.value == 'v' }.forEach { (y, x), c ->
            val check = (if (y == length - 1) 0 else y + 1) to x
            if (this[check] == 'v' || newMap[check] == '>') {
                newMap[y to x] = c
            } else {
                newMap[check] = c
            }
        }

        return newMap
    }

    private fun String.parse():  Triple<Int, Int, Map<Pair<Int, Int>, Char>> {
        val map = mutableMapOf<Pair<Int, Int>, Char>()
        val length = lines().size
        val width = lines().first().length
        lines().forEachIndexed{ y, line -> line.forEachIndexed { x, c -> if (c == '>' || c == 'v') map[y to x] = c}}
        return  Triple(length, width, map)
    }
}

fun main() {
    SeaCucumber().runAll()
}