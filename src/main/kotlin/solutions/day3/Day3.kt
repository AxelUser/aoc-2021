package solutions.day3

import solutions.Solution

@OptIn(ExperimentalUnsignedTypes::class)
class Day3 : Solution() {
    override val inputDir: String = "day3"


    override fun part1(input: String): String {
        val lines = input.lines()
        val size = lines[0].length
        val gamma = lines.map { it.toCharArray().map { c -> if (c == '1') 1 else -1 } }
            .fold(List(size){0}) { stat, cur -> cur.zip(stat) { a, b -> a + b} }
            .fold("") {s, count -> s.plus(if (count <= 0) '0' else '1')}
            .toUInt(2)

        val eps = gamma.inv() and (UInt.MAX_VALUE shr (UInt.SIZE_BITS - size))

        return (gamma * eps).toString()
    }

    override fun part2(input: String): String {
        val (ones, zeros) = countCommon(input.lines(), 0)
        val oxygen = search(if (ones.count() >= zeros.count()) ones else zeros, Parameter.Oxygen, 1)
        val co2 = search(if (zeros.count() <= ones.count()) zeros else ones, Parameter.CO2, 1)

        return (oxygen * co2).toString()
    }

    private enum class Parameter {
        Oxygen,
        CO2
    }

    private fun search(values: List<String>, searchFor: Parameter, pos: Int): UInt {
        if (values.count() <= 1)
            return values[0].toUInt(2)

        val (ones, zeros) = countCommon(values, pos)

        val filtered = if (searchFor == Parameter.Oxygen) {
            if (ones.count() >= zeros.count()) ones else zeros
        } else {
            if (zeros.count() <= ones.count()) zeros else ones
        }

        return search(filtered, searchFor, pos + 1)
    }

    private fun countCommon(values: Iterable<String>, pos: Int): Pair<List<String>, List<String>> {
        val ones = mutableListOf<String>()
        val zeros = mutableListOf<String>()

        values.forEach { if (it[pos] == '1') ones.add(it) else zeros.add(it) }
        return Pair(ones, zeros)
    }
}