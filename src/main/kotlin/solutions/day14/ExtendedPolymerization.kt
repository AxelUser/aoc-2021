package solutions.day14

import solutions.BaseSolution

class ExtendedPolymerization: BaseSolution() {
    override fun part1(input: String): Any {
        return input.solve(10)
    }

    override fun part2(input: String): Any {
        return input.solve(40)
    }

    private fun String.solve(steps: Int): Long {
        return parseInput().let { (template, templatePairs, rules) ->
            val frequencies = (0 until steps)
                .fold(templatePairs) { pairs, _ -> rules.applyTo(pairs) }
                .lettersFrequencies(template.first(), template.last())
            val min = frequencies.minOf { (_, v) -> v }
            val max = frequencies.maxOf { (_, v) -> v }

            max - min
        }
    }

    private fun Map<String, Char>.applyTo(pairs: Map<String, Long>): Map<String, Long> {
        return pairs.flatMap { (pair, count) ->
            val inserted = getValue(pair)
            listOf("${pair[0]}$inserted" to count, "$inserted${pair[1]}" to count)
        }.groupBy { it.first }.mapValues { (_, counts) -> counts.sumOf { it.second } }
    }

    private fun Map<String, Long>.lettersFrequencies(leftmost: Char, rightmost: Char): Map<Char, Long> {
        return flatMap { (pair, count) -> pair.map { it to count } }
            .groupingBy { (letter, _) -> letter }
            .fold(0L) { sum, (_, pairCount) -> sum + pairCount }
            .mapValues { (letter, pairCount) ->
                when(letter) {
                    leftmost -> (pairCount + 1) / 2
                    rightmost -> (pairCount + 1) / 2
                    else -> pairCount / 2
                }
            }
    }

    private fun String.parseInput(): Triple<String, Map<String, Long>, Map<String, Char>> {
        val (template, rulesGroup) = this.split("${System.lineSeparator()}${System.lineSeparator()}", limit = 2)

        val pairs = template.windowed(size = 2).groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }
        val rules = rulesGroup.lines().map { l -> l.split("->", limit = 2).map { it.trim() } }
            .associate { (pair, ins) -> pair to ins[0] }

        return Triple(template.trim(), pairs, rules)
    }
}

fun main() {
    ExtendedPolymerization().runAll()
}