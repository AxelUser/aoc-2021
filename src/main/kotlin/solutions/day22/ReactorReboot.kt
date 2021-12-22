package solutions.day22

import solutions.BaseSolution
import kotlin.math.abs

private infix fun IntRange.intersect(other: IntRange): IntRange? {
    val min = maxOf(this.first, other.first)
    val max = minOf(this.last, other.last)
    return if (min <= max) return min .. max else null
}

private data class Cuboid(val isOn: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange){
    val energy = (if (isOn) 1L else -1L) *
            abs(xRange.last - xRange.first + 1) *
            abs(yRange.last - yRange.first + 1) *
            abs(zRange.last - zRange.first + 1)

    infix fun apply(other: Cuboid): Cuboid? {
        val intersectedX = xRange intersect other.xRange
        val intersectedY = yRange intersect other.yRange
        val intersectedZ = zRange intersect other.zRange

        return if (intersectedX == null || intersectedY == null || intersectedZ == null) null
        else Cuboid(!isOn, intersectedX, intersectedY, intersectedZ)
    }
}

class ReactorReboot: BaseSolution() {
    override fun part1(input: String): Any {
        val cuboids = input.parse()

        val turnedOn = mutableSetOf<Triple<Int, Int, Int>>()

        for (cuboid in cuboids) {
            val (isOn, xRange, yRange, zRange) = cuboid
            if (abs(xRange.last) > 50) continue
            for (x in xRange) {
                for (y in yRange) {
                    for (z in zRange) {
                        val cube = Triple(x, y, z)
                        if (isOn) {
                            turnedOn.add(cube)
                        } else {
                            turnedOn.remove(cube)
                        }
                    }
                }
            }
        }

        return turnedOn.count()
    }

    override fun part2(input: String): Any {
        return input.parse().fold(sequenceOf<Cuboid>()) { merged, unmergedCuboid ->
            sequence {
                for (mergedCuboid in merged) {
                    yield(mergedCuboid)
                    val appliedAboveMerged = (mergedCuboid apply unmergedCuboid) ?: continue
                    yield(appliedAboveMerged)
                }
                if (unmergedCuboid.isOn) yield(unmergedCuboid)
            }
        }.sumOf { it.energy }
    }

    private fun String.parse(): List<Cuboid> {
        val rgx = "-?\\d+".toRegex()
        return lines().map { l ->
            val isOn = l.contains("on")
            rgx.findAll(l).map { it.value.toInt() }.toList().let { Cuboid(isOn, it[0]..it[1], it[2] .. it[3], it[4] .. it[5]) }
        }
    }
}

fun main() {
    ReactorReboot().runAll()
}