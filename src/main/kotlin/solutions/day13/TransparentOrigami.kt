package solutions.day13

import solutions.BaseSolution

class TransparentOrigami: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parse().let { (points, commands) -> points.foldPaper(commands.take(1)).count() }
    }

    override fun part2(input: String): Any {
        input.parse().also { (points, commands) -> points.foldPaper(commands).print() }
        return "^ printed above ^"
    }

    private fun Set<Pair<Int, Int>>.foldPaper(commands: List<Command>): Set<Pair<Int, Int>> {
        return commands.fold(this) {set, command -> set.map { p -> command.reflect(p) }.toSet() }
    }

    private fun Set<Pair<Int, Int>>.print() {
        for (y in 0 .. maxOf { (_, y) -> y }) {
            for (x in 0 .. maxOf { (x, _) -> x }) {
                print(if(contains(Pair(x, y))) "█" else "░")
            }
            println()
        }
    }

    private data class Command(val foldLine: Char, val lineNumber: Int) {
        fun reflect(point: Pair<Int, Int>): Pair<Int, Int> {
            val (x, y) = point

            return when(foldLine) {
                'x' -> if(x > lineNumber) Pair(lineNumber - (x - lineNumber), y) else point
                else -> if(y > lineNumber) Pair(x, lineNumber - (y - lineNumber)) else point
            }
        }
    }

    private fun String.parse(): Pair<Set<Pair<Int, Int>>, List<Command>> {
        val (pointsGroup, commandsGroup) = this.split("${System.lineSeparator()}${System.lineSeparator()}")
        val matrix = pointsGroup.lines().map { l -> l.split(',').let { (x, y) -> x.toInt() to y.toInt() } }.toSet()
        val commands = commandsGroup.lines().map { l -> l.removePrefix("fold along ").split('=').let { (c, n) -> Command(c[0], n.toInt()) } }

        return Pair(matrix, commands)
    }
}

fun main() {
    TransparentOrigami().runAll()
}