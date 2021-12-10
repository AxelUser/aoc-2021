package solutions.day10

import solutions.BaseSolution
import java.util.*

class SyntaxScoring: BaseSolution() {
    override fun part1(input: String): Any {
        return input.lines().sumOf { getErrorScore(it) }
    }

    override fun part2(input: String): Any {
        return input.lines().filter { getErrorScore(it) == 0 }.map { getFixScore(it) }.sorted().let { it[it.count() / 2] }
    }

    private fun getFixScore(line: String): Long  {
        val braces = Stack<Char>()

        for (cur in line) {
            if (cur in arrayOf('(', '[', '{', '<')) {
                braces.push(cur)
            } else {
                braces.pop()
            }
        }

        var totalScore = 0L
        while (braces.isNotEmpty()) {
            val cost = when(braces.pop()) {
                '(' -> 1
                '[' -> 2
                '{' -> 3
                '<' -> 4
                else -> error("Unexpected")
            }

            totalScore = totalScore * 5 + cost
        }
        return totalScore
    }

    private fun getErrorScore(line: String): Int {
        val braces = Stack<Char>()

        for (cur in line) {
            if (cur in arrayOf('(', '[', '{', '<')) {
                braces.push(cur)
            } else {
                if(!braces.empty()) {
                    val open = braces.pop()
                    if (open == '(' && cur == ')') continue
                    if (open == '{' && cur == '}') continue
                    if (open == '[' && cur == ']') continue
                    if (open == '<' && cur == '>') continue
                }
                return when(cur) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> error("Unexpected")
                }
            }
        }
        return 0
    }
}

fun main() {
    SyntaxScoring().runAll()
}