package solutions.day17

import solutions.BaseSolution
import kotlin.math.sign

class TrickShot: BaseSolution() {
    override fun part1(input: String): Any = input.parse().let { (targetX, targetY) ->
        return solve(targetX, targetY).maxOf { it }
    }

    override fun part2(input: String): Any = input.parse().let { (targetX, targetY) ->
        return solve(targetX, targetY).count()
    }

    private fun solve(targetX: Pair<Int, Int>, targetY: Pair<Int, Int>): Sequence<Int> {
        return sequence {
            for (vx in 1 .. targetX.second) {
                for (vy in (targetY.first * 2) .. -targetY.first) {
                    val maxY = getMaxYIfHit(vx, vy, targetX, targetY)
                    if (maxY != null) yield(maxY)
                }
            }
        }
    }

    private fun getMaxYIfHit(initialVelocityX: Int, initialVelocityY: Int, targetX: Pair<Int, Int>, targetY: Pair<Int, Int>): Int? {
        var vx = initialVelocityX
        var vy = initialVelocityY
        var x = 0
        var y = 0
        var maxY = 0

        while (x < targetX.second && y > targetY.first) {
            x += vx
            y += vy

            maxY = maxOf(y, maxY)

            vx -= vx.sign
            vy -= 1

            if ((x in targetX.first .. targetX.second) && (y in targetY.first .. targetY.second)) {
                return maxY
            }
        }

        return null
    }

    private fun String.parse(): Pair<Pair<Int,Int>, Pair<Int, Int>> = "-?\\d+".toRegex().let {
            it.findAll(this).map { m -> m.value.toInt() }.toList().let { (x1, x2, y1, y2) -> (x1 to x2) to (y1 to y2) }
    }
}

fun main() {
    TrickShot().runAll()
}