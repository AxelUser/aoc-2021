package solutions

import java.io.File
import java.nio.file.Path

abstract class Solution {
    abstract fun part1(input: String): String
    abstract fun part2(input: String): String

    fun runAll() {
        val name = this.javaClass.packageName.substringAfterLast('.')
        val solutionsDir = Path.of(File("").absolutePath, "src", "main", "kotlin", "solutions").toAbsolutePath().toString()
        println(name.replace("\\D+".toRegex(), "Day ").plus(" results:"))
        println("Part 1:")
        println(part1(Path.of(solutionsDir, name, "part1").toFile().readText()))
        println()
        println("Part 2:")
        println(part2(Path.of(solutionsDir, name, "part2").toFile().readText()))
    }
}