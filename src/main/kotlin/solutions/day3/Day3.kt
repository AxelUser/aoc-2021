package solutions.day3

import solutions.BaseSolution

@OptIn(ExperimentalUnsignedTypes::class)
class Day3 : BaseSolution() {


    override fun part1(input: String): String {
        val lines = input.lines()
        val size = lines[0].length
        val gamma = lines.map { it.toCharArray().map { c -> if (c == '1') 1 else -1 } }
            .fold(List(size){0}) { stat, cur -> cur.zip(stat) { a, b -> a + b} }
            .fold(0U) { n, count -> n shl 1 or if (count <= 0) 0U else 1U }

        val eps = gamma.inv() and (UInt.MAX_VALUE shr (UInt.SIZE_BITS - size))

        return (gamma * eps).toString()
    }

    override fun part2(input: String): String {
        val oxygenPredicate = { ones: List<String>, zeros: List<String> -> if (ones.count() >= zeros.count()) ones else zeros}
        val co2Predicate = { ones: List<String>, zeros: List<String> -> if (zeros.count() <= ones.count()) zeros else ones}

        val (ones, zeros) = countValues(input.lines(), 0)

        val oxygen = search(oxygenPredicate(ones, zeros), 1, oxygenPredicate)
        val co2 = search(co2Predicate(ones, zeros), 1, co2Predicate)

        return (oxygen * co2).toString()
    }


    private fun search(values: List<String>, pos: Int, getNext: (ones: List<String>, zeros: List<String>) -> List<String>): UInt {
        if (values.count() <= 1)
            return values[0].toUInt(2)

        val (ones, zeros) = countValues(values, pos)
        return search(getNext(ones, zeros),pos + 1, getNext)
    }

    private fun countValues(values: Iterable<String>, pos: Int): Pair<List<String>, List<String>> {
        return values.groupBy { it[pos] }.let { Pair(it['1']?: emptyList(), it['0']?: emptyList()) }
    }
}