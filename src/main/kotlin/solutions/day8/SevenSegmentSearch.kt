package solutions.day8

import solutions.BaseSolution

class SevenSegmentSearch: BaseSolution() {
    override fun part1(input: String): String {
        val rgx = "\\w+".toRegex()
        val values = input.lines()
            .map { l -> rgx.findAll(l.substringAfter('|'))
                .map { m -> m.value.toHashSet() }}.flatMap { it }

        return values
            .count { s -> s.count() in arrayOf(2, 3, 4, 7) }.toString()

    }

    override fun part2(input: String): String {
        val rgx = "\\w+".toRegex()
        return input.lines().map { rgx.findAll(it).map { m -> m.value } }
            .map { s -> s.chunked(10).toList() }
            .map { ch -> ch[0] to ch[1] }
            .getNumbers()
            .sum().toString()
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
            setOf('a', 'b', 'c', 'e', 'f', 'g')      to 0,
            setOf('c', 'f')                          to 1,
            setOf('a', 'c', 'd', 'e', 'g')           to 2,
            setOf('a', 'c', 'd', 'f', 'g')           to 3,
            setOf('b', 'd', 'c', 'f')                to 4,
            setOf('a', 'b', 'd', 'f', 'g')           to 5,
            setOf('a', 'b', 'd', 'e', 'f', 'g')      to 6,
            setOf('a', 'c', 'f')                     to 7,
            setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to 8,
            setOf('a', 'b', 'c', 'd', 'f', 'g')      to 9,
        )

        val input = this

        return sequence {
            for ((unique, output) in input) {
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
                        if(segmentsCounter.computeIfPresent(c) { _, v -> v + 1 } == null) {
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

                yield(output.map { s -> foundSetsPerDigit.getValue(s.toSet()) }.reduce{ n, d -> n * 10 + d })
            }
        }
    }
}