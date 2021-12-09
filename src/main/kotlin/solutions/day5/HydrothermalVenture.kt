package solutions.day5

import solutions.BaseSolution

class HydrothermalVenture: BaseSolution() {
    override fun part1(input: String): Any {
        val rgx = "\\d+".toRegex()
        return input.lines()
            .map { rgx.findAll(it).map { m -> m.value.toInt() }.chunked(2).map { (a, b) -> Point(a, b) }.toList() }
            .filter { (a, b) -> a.x == b.x || a.y == b.y }
            .map { (a, b) -> a .. b}
            .flatten()
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 2 }
            .count()
    }

    override fun part2(input: String): Any {
        val rgx = "\\d+".toRegex()
        return input.lines()
            .map { rgx.findAll(it).map { m -> m.value.toInt() }.chunked(2).map { (a, b) -> Point(a, b) }.toList() }
            .map { (a, b) -> a .. b}
            .flatten()
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 2 }
            .count()
    }
}

data class Point(val x: Int, val y: Int){
    operator fun rangeTo(other: Point): List<Point> =
        when {
            other.x == x -> (minOf(y, other.y) .. maxOf(y, other.y)).map { Point(x, it) }
            other.y == y -> (minOf(x, other.x) .. maxOf(x, other.x)).map { Point(it, y) }
            other.x - x == other.y - y -> (minOf(x, other.x) .. maxOf(x, other.x))
                .zip(minOf(y, other.y) .. maxOf(y, other.y)).map { (a, b) -> Point(a, b) }
            // if x's and y's are going in different directions
            else -> (minOf(x, other.x) .. maxOf(x, other.x))
                .zip(maxOf(y, other.y) downTo minOf(y, other.y)).map { (a, b) -> Point(a, b) }
        }
}

fun main() {
    HydrothermalVenture().runAll()
}