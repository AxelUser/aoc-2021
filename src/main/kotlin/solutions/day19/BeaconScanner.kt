package solutions.day19

import solutions.BaseSolution
import kotlin.math.abs

class BeaconScanner: BaseSolution() {
    override fun part1(input: String): Any {
        val scanners = input.parse().toList()
        val rotations = getAllRotations().toList()
        val (beacons, scannersPositions) = buildMap(scanners, rotations)
        return "Beacons: ${beacons.size}, Max distance between scanners: ${maxDistance(scannersPositions)}"
    }

    override fun part2(input: String): Any {
        return "Result computed in part 1"
    }

    private data class Vector3(private val axes: List<Int>) {
        constructor(x: Int, y: Int, z: Int): this(listOf(x, y, z))

        operator fun get(index: Int): Int {
            return axes[index]
        }

        fun traverse(to: Vector3): Vector3 {
            return Vector3(this.axes.zip(to.axes){ a, b -> a + b })
        }

        fun to(target: Vector3): Vector3 {
            return Vector3(target.axes.zip(this.axes){ a, b -> a - b })
        }

        fun manhattanFromZero(): Int {
            return axes.sumOf { abs(it) }
        }
    }

    private data class Rotation(val inversionPermutation: Vector3, val axesPermutation: Vector3) {
        fun applyTo(vec: Vector3): Vector3 {
            val new = mutableListOf(0, 0, 0)
            for (i in 0 until 3) {
                new[i] = inversionPermutation[i] * vec[axesPermutation[i]]
            }

            return Vector3(new)
        }
    }

    private fun getAllRotations(): Sequence<Rotation> {
        val inversions = listOf(
            Vector3(1, 1, 1),
            Vector3(1, 1, -1),
            Vector3(1, -1, 1),
            Vector3(1, -1, -1),
            Vector3(-1, 1, 1),
            Vector3(-1, 1, -1),
            Vector3(-1, -1, 1),
            Vector3(-1, -1, -1)
        )

        val movedAxes = listOf(
            Vector3(0, 1, 2),
            Vector3(0, 2, 1),
            Vector3(1, 0, 2),
            Vector3(2, 0, 1),
            Vector3(1, 2, 0),
            Vector3(2, 1, 0)
        )

        return sequence {
            for (inv in inversions) {
                for (axes in movedAxes) {
                    yield(Rotation(inv, axes))
                }
            }
        }
    }

    private fun match(scanner: List<Vector3>, otherScanner: List<Vector3>, rotations: List<Rotation>): Pair<List<Vector3>, Vector3>? {
        for (rotation in rotations) {
            for (p1 in scanner) {
                for (p2 in otherScanner) {
                    val transpositionP2 = rotation.applyTo(p2).to(p1)
                    var count = 0
                    val notMatched = mutableListOf<Vector3>()
                    for (pp2 in otherScanner) {
                        var hit = false
                        val transpositionPP2 = rotation.applyTo(pp2).traverse(transpositionP2)
                        for (pp1 in scanner) {
                            if (pp1 == transpositionPP2) {
                                count++
                                hit = true
                            }
                        }
                        if (!hit) {
                            notMatched.add(transpositionPP2)
                        }
                    }

                    if (count > 11) {
                        return notMatched to transpositionP2
                    }
                }
            }
        }

        return null
    }

    private data class Scanner(val number: Int, val beacons: List<Vector3>)

    private fun buildMap(scanners: List<Scanner>, rotations: List<Rotation>): Pair<List<Vector3>, List<Vector3>> {
        val scannersPositions = mutableListOf(Vector3(0, 0, 0))
        val merged = scanners.first().beacons.toMutableList()
        val success = mutableMapOf(scanners.first() to true)

        while (success.size < scanners.size) {
            for (scanner in scanners) {
                if (success.containsKey(scanner)) continue

                val matchResult = match(merged, scanner.beacons, rotations)
                if (matchResult != null) {
                    val (notMatched, pos) = matchResult
                    println("Merged scanner ${scanner.number} (${scanners.size - success.size} left)")
                    merged.addAll(notMatched)
                    success[scanner] = true
                    scannersPositions.add(pos)
                }
            }
        }

        return merged to scannersPositions
    }

    private fun maxDistance(scannersPositions: List<Vector3>): Int {
        var max = 0
        for (scanner1 in scannersPositions) {
            for (scanner2 in scannersPositions) {
                if (scanner1 == scanner2) continue
                max = maxOf(max, scanner1.to(scanner2).manhattanFromZero())
            }
        }
        return max
    }

    private fun String.parse(): Sequence<Scanner> {
        val scanners = split("${System.lineSeparator()}${System.lineSeparator()}").map { it.trim() }
        val rgx = "-?\\d+".toRegex()

        return sequence {
            scanners.forEachIndexed { n, scanner ->
                val beacons = scanner.lines().drop(1).map { l -> rgx.findAll(l).map { m -> m.value.toInt() }.let { Vector3(it.toList()) } }
                yield(Scanner(n, beacons))
            }
        }
    }
}

fun main() {
    BeaconScanner().runAll()
}