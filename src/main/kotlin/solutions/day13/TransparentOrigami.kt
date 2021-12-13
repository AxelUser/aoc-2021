package solutions.day13

import solutions.BaseSolution

class TransparentOrigami: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parse().let { (matrix, commands) -> matrix.foldPaper(commands.take(1)) }
    }

    override fun part2(input: String): Any {
        input.parse().let { (matrix, commands) -> matrix.foldPaper(commands, printFinal = true) }
        return "Printed above"
    }

    private fun Array<BooleanArray>.foldPaper(commands: List<Command>, printFinal: Boolean = false): Int {
        var length = count()
        var width = first().count()

        for ((foldLine, lineNumber) in commands) {
            when(foldLine) {
                'x' -> {
                    for (y in 0 until length) {
                        for (x in (lineNumber + 1) until  width ){
                            val mirroredX = width - (x + 1)
                            this[y][mirroredX] = this[y][x] || this[y][mirroredX]
                            this[y][x] = false
                        }
                    }
                    width = lineNumber
                }
                'y' -> {
                    for (y in (lineNumber + 1) until length) {
                        val mirroredY = length - (y + 1)
                        for (x in 0 until  width ){
                            this[mirroredY][x] = this[y][x] || this[mirroredY][x]
                            this[y][x] = false
                        }
                    }
                    length = lineNumber
                }
            }

        }

        var count = 0
        for (y in 0 until length) {
            for (x in 0 until width) {
                if(this[y][x]) count++
            }
        }

        if(printFinal) print(length, width)

        return count
    }

    private fun Array<BooleanArray>.print(length: Int, width: Int) {
        val s = take(length).joinToString(separator = System.lineSeparator()) { row ->
            row.take(width).map { if (it) '#' else '.' }.joinToString(separator = "")
        }
        println(s)
        println()
    }

    private data class Command(val foldLine: Char, val lineNumber: Int)

    private fun String.parse(): Pair<Array<BooleanArray>, List<Command>> {
        val (pointsGroup, commandsGroup) = this.split("${System.lineSeparator()}${System.lineSeparator()}")

        val matrix = pointsGroup.run {
            val points = lines().map { l -> l.split(',').let { (x, y) -> x.toInt() to y.toInt() } }
            val width = points.maxOf { (x, _) -> x } + 1
            val length = points.maxOf { (_, y) -> y } + 1

            Array(length){BooleanArray(width)}.apply {
                for ((x, y) in points) {
                    this[y][x] = true
                }
            }
        }

        val commands = commandsGroup.lines().map { l -> l.removePrefix("fold along ").split('=').let { (c, n) -> Command(c[0], n.toInt()) } }

        return matrix to commands
    }
}

fun main() {
    TransparentOrigami().runAll()
}