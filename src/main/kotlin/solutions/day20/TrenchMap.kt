package solutions.day20

import solutions.BaseSolution

class TrenchMap: BaseSolution() {
    override fun part1(input: String): Any {
        return input.solve(2)
    }

    override fun part2(input: String): Any {
        return input.solve(50)
    }

    private fun String.solve(steps: Int): Int = parse().let { (alg, matrix, set) -> runSteps(steps, alg, set, matrix).count() }

    private fun runSteps(steps: Int, alg: IntArray, originalMap: Set<Point>, originalMatrix: VirtualMatrix): Set<Point> {
        var map = originalMap
        var matrix = originalMatrix
        var outOfRangeBit = 0

        repeat(steps) {
            val (newMap, newMatrix) = enhanceImage(alg, map, matrix, outOfRangeBit and alg[0])
            map = newMap
            matrix = newMatrix
            outOfRangeBit = outOfRangeBit.inv() and 511
        }

        return map
    }

    private fun enhanceImage(alg: IntArray, originalMap: Set<Point>, originalMatrix: VirtualMatrix, outOfRangeBit: Int): Pair<Set<Point>, VirtualMatrix> {
        val enhanced = mutableSetOf<Point>()
        val expandedMatrix = originalMatrix.expand()
        expandedMatrix.forEach {
            var idx = 0
            for (dy in -1 .. 1) {
                for (dx in -1 .. 1) {
                    val cur = it + Point(dx, dy)
                    idx = idx shl 1

                    if (originalMap.contains(cur)) {
                        idx = idx or 1
                    } else if (!originalMatrix.contains(cur)) {
                        idx = idx or outOfRangeBit
                    }
                }
            }
            if (alg[idx] == 1) {
                enhanced.add(it)
            }
        }

        return Pair(enhanced, expandedMatrix)
    }

    private fun String.parse(): Triple<IntArray, VirtualMatrix, Set<Point>> {
        val (alg, initialValues) = split("${System.lineSeparator()}${System.lineSeparator()}", limit = 2)
            .map { it.lines().map { l -> l.map { c -> if (c == '#') 1 else 0 }.toIntArray() }.toTypedArray() }

        val matrix = VirtualMatrix(Point.Zero, Point(initialValues[0].size - 1, initialValues.size - 1))
        val set = initialValues.mapIndexed { y, row -> row.foldIndexed(mutableListOf<Point>()){ x, s, value ->
            if (value == 1)
                s.add(Point(x, y))

            return@foldIndexed s
        } }.flatten().toSet()

        return Triple(alg.single(), matrix, set)
    }

    private data class Point(val x: Int, val y: Int) {
        operator fun rangeTo(other: Point): Sequence<Point> = sequence {
            for (cy in y .. other.y) {
                for (cx in x .. other.x) {
                    yield(Point(cx, cy))
                }
            }
        }

        operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)

        operator fun plus(num: Int): Point = Point(x + num, y + num)

        operator fun minus(num: Int): Point = Point(x - num, y - num)

        companion object {
            val Zero = Point(0, 0)
        }
    }

    private class VirtualMatrix(val topLeft: Point, val bottomRight: Point) {
        fun expand(n: Int = 1): VirtualMatrix = VirtualMatrix(topLeft - n, bottomRight + n)

        fun forEach(func: (Point) -> Unit) {
            for (p in topLeft .. bottomRight) func(p)
        }

        fun contains(point: Point): Boolean {
            return point.run { x >= topLeft.x && x <= bottomRight.x && y >= topLeft.y && y <= bottomRight.y }
        }

        fun print(setPixels: Set<Point>) {
            for (y in topLeft.y .. bottomRight.y) {
                for (x in topLeft.x .. bottomRight.x) {
                    if (setPixels.contains(Point(x, y))) {
                        print("█")
                    } else {
                        print("░")
                    }
                }
                println()
            }
            println()
        }
    }
}

fun main() {
    TrenchMap().runAll()
}