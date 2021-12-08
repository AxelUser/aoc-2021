package solutions

import java.io.File
import java.nio.file.Path

abstract class BaseSolution {
    abstract fun part1(input: String): String
    abstract fun part2(input: String): String

    fun runAll() {
        val name = this.javaClass.packageName.substringAfterLast('.')
        val solutionsDir = Path.of(File("").absolutePath, "src", "main", "kotlin", "solutions").toAbsolutePath().toString()
        println(name.replace("\\D+".toRegex(), "Day ").plus(" results:"))
        val input = Path.of(solutionsDir, name, "input").toFile().readText()
        println("Part 1:")
        println(part1(input))
        println()
        println("Part 2:")
        println(part2(input))
    }
}