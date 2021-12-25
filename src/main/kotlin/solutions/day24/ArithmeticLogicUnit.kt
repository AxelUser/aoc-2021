package solutions.day24

import solutions.BaseSolution

class ArithmeticLogicUnit: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parse().solve(0, LongArray(4), mutableMapOf(), 9L downTo 1).toString()
    }

    override fun part2(input: String): Any {
        return input.parse().solve(0, LongArray(4), mutableMapOf(), 1L .. 9).toString()
    }

    private data class ResultKey(val lineIndex: Int, val regW: Long, val regX: Long, val regY: Long, val regZ: Long)

    private fun List<Instruction>.solve(lineIndex: Int, regs: LongArray, results: MutableMap<ResultKey, String?>, range: LongProgression): String? {
        val key = ResultKey(lineIndex, regs[0], regs[1], regs[2], regs[3])
        if (results.containsKey(key)) return results[key]

        for (idx in lineIndex until size) {
            val (operation, reg, otherReg, value) = this[idx]
            val arg = if (otherReg == -1) value else regs[otherReg]
            when(operation) {
                "inp" -> {
                    for (n in range) {
                        regs[reg] = n
                        val number = solve(idx + 1, regs.copyOf(), results, range)
                        if (number != null) {
                            results[key] = "${n}${number}"
                            return "${n}${number}"
                        }
                    }
                    results[key] = null
                    return null
                }
                "add" -> regs[reg] += arg
                "mul" -> regs[reg] *= arg
                "div" -> {
                    if (arg == 0L)
                    {
                        results[key] = null
                        return null
                    }
                    regs[reg] /= arg
                }
                "mod" -> {
                    if (regs[reg] < 0 || arg <= 0)
                    {
                        results[key] = null
                        return null
                    }
                    regs[reg] %= arg
                }
                "eql" -> {
                    regs[reg] = if (regs[reg] == arg) 1 else 0
                }
                else -> error("unexpected operation $operation")
            }
        }

        results[key] = if (regs.last() == 0L) "" else null
        return results[key]
    }

    private data class Instruction(val operation: String, val reg: Int, val otherReg: Int, val value: Long)

    private fun String.parse(): List<Instruction> {
        return lines().map { l ->
            val split = l.split(' ')
            var otherReg = -1
            var value = 0L

            if (split.size == 3) {
                if (split[2][0].isLetter()) {
                    otherReg = split[2][0] - 'w'
                } else {
                    value = split[2].toLong()
                }
            }

            Instruction(split[0], split[1][0] - 'w', otherReg, value)
        }
    }
}

fun main() {
    ArithmeticLogicUnit().runAll()
}