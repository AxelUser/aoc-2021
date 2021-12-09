package solutions.day8

import solutions.BaseSolution

class SevenSegmentSearch : BaseSolution() {
    override fun part1(input: String): Any {
        val rgx = "\\w+".toRegex()
        return input.lines()
            .map { l ->
                rgx.findAll(l.substringAfter('|'))
                    .map { m -> m.value.toHashSet() }
            }.flatMap { it }.count { s -> s.count() in arrayOf(2, 3, 4, 7) }
    }

    override fun part2(input: String): Any {
        val rgx = "\\w+".toRegex()
        return input.lines().map { rgx.findAll(it).map { m -> m.value } }
            .map { s -> s.chunked(10).toList() }
            .map { ch -> ch[0] to ch[1] }
            .getNumbersOptimized()
            .sum()
    }

    private fun List<Pair<List<String>, List<String>>>.getNumbersOptimized(): Sequence<Int> {
        return sequence {
            for ((representations, output) in this@getNumbersOptimized) {
                val masksByCount = representations
                        .map { r -> r.fold(0) { acc, c -> acc or (1 shl (c - 'a')) } }
                        .groupBy { it.countOneBits() }

                val masks = IntArray(10)
                masks[1] = masksByCount.getValue(2).single()
                masks[4] = masksByCount.getValue(4).single()
                masks[7] = masksByCount.getValue(3).single()
                masks[8] = masksByCount.getValue(7).single()

                // checking 2, 3 and 5
                masksByCount.getValue(5).forEach { m ->
                    when {
                        (m and masks[1]).countOneBits() == 2 -> masks[3] = m
                        else -> when {
                            (m and masks[4]).countOneBits() == 2 -> masks[2] = m
                            else -> masks[5] = m
                        }
                    }
                }

                // checking 0, 6 and 9
                masksByCount.getValue(6).forEach { m ->
                    when {
                        (m and masks[1]).countOneBits() == 1 -> masks[6] = m
                        else -> when {
                            (m and masks[4]).countOneBits() == 3 -> masks[0] = m
                            else -> masks[9] = m
                        }
                    }
                }

                val mappedDigits = masks.mapIndexed{ dig, mask -> mask to dig }.toMap()

                val num = output.map { r -> r.fold(0) { acc, c -> acc or (1 shl (c - 'a')) } }
                    .map { mappedDigits.getValue(it) }
                    .reduce { num, d -> num * 10 + d }

                yield(num)
            }
        }
    }

    /*
         aaaa
        b    c
        b    c
         dddd
        e    f
        e    f
         gggg
    */
    private fun List<Pair<List<String>, List<String>>>.getNumbers(): Sequence<Int> {
        val normalizedSegmentsPerDigit = mapOf(
            setOf('a', 'b', 'c', 'e', 'f', 'g') to 0,
            setOf('c', 'f') to 1,
            setOf('a', 'c', 'd', 'e', 'g') to 2,
            setOf('a', 'c', 'd', 'f', 'g') to 3,
            setOf('b', 'd', 'c', 'f') to 4,
            setOf('a', 'b', 'd', 'f', 'g') to 5,
            setOf('a', 'b', 'd', 'e', 'f', 'g') to 6,
            setOf('a', 'c', 'f') to 7,
            setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to 8,
            setOf('a', 'b', 'c', 'd', 'f', 'g') to 9,
        )

        return sequence {
            for ((unique, output) in this@getNumbers) {
                var set1 = setOf<Char>()
                var set4 = setOf<Char>()
                var set7 = setOf<Char>()
                var set8 = setOf<Char>()
                val segmentsCounter = mutableMapOf<Char, Int>()

                for (display in unique) {
                    when (display.length) {
                        2 -> set1 = display.toHashSet()
                        3 -> set7 = display.toHashSet()
                        4 -> set4 = display.toHashSet()
                        7 -> set8 = display.toHashSet()
                    }

                    for (c in display) {
                        if (segmentsCounter.computeIfPresent(c) { _, v -> v + 1 } == null) {
                            segmentsCounter[c] = 1
                        }
                    }
                }

                val normalizedToFoundSeg = mutableMapOf<Char, Char>()
                normalizedToFoundSeg['b'] = segmentsCounter.filter { (_, v) -> v == 6 }.keys.single()
                normalizedToFoundSeg['e'] = segmentsCounter.filter { (_, v) -> v == 4 }.keys.single()
                normalizedToFoundSeg['f'] = segmentsCounter.filter { (_, v) -> v == 9 }.keys.single()
                normalizedToFoundSeg['c'] = set1.minus(normalizedToFoundSeg.getValue('f')).single()
                normalizedToFoundSeg['a'] = (set7 - set1).single()
                normalizedToFoundSeg['g'] = (set8 - set4 - set7).minus(normalizedToFoundSeg.getValue('e')).single()
                normalizedToFoundSeg['d'] = (set8 - normalizedToFoundSeg.values).single()

                val foundSetsPerDigit = normalizedSegmentsPerDigit
                    .map { (n, d) -> n.map { normalizedToFoundSeg.getValue(it) }.toSet() to d }
                    .toMap()

                yield(output.map { s -> foundSetsPerDigit.getValue(s.toSet()) }.reduce { n, d -> n * 10 + d })
            }
        }
    }
}

fun main() {
    SevenSegmentSearch().runAll()
}