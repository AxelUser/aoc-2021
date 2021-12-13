package solutions

import java.io.File
import java.nio.file.Path
import kotlin.math.roundToLong
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class BaseSolution {
    abstract fun part1(input: String): Any
    abstract fun part2(input: String): Any

    fun runAll() {
        val name = this.javaClass.packageName.substringAfterLast('.')
        val solutionsDir = Path.of(File("").absolutePath, "src", "main", "kotlin", "solutions").toAbsolutePath().toString()
        println(name.replace("\\D+".toRegex(), "Day ").plus(" results:"))
        val input = Path.of(solutionsDir, name, "input").toFile().readText()
        run("Part 1") { part1(input) }
        println()
        run("Part 2") { part2(input) }
    }

    @OptIn(ExperimentalTime::class)
    fun run(name: String, func: () -> Any) {
        val elapsed = measureTimedValue { func() }
        println("$name:")
        println("\tResult: ${elapsed.value}")
        println("\tTime: ${elapsed.duration.inMilliseconds.roundToLong()} ms")
    }
}