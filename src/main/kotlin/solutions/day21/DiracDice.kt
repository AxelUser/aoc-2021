package solutions.day21

import solutions.BaseSolution

private infix fun Int.move(rolledSum: Int): Int = ((this + rolledSum - 1) % 10) + 1

class DiracDice: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parse().let { (p1, p2) -> solvePart1(p1, p2) }
    }

    override fun part2(input: String): Any {
        return input.parse().let { (p1, p2) -> solvePart2(p1, p2) }
    }

    private fun solvePart1(initialP1: Int, initialP2: Int): Int {
        val p1 = Player(initialP1)
        val p2 = Player(initialP2)
        val die = Die()
        while(true) {
            p1.move(die.roll())
            if (p1.isWon()) return p2.score * die.rolls

            p2.move(die.roll())
            if (p2.isWon()) return p1.score * die.rolls
        }
    }

    private data class Player(var position: Int, var score: Int = 0)  {
        fun move(steps: Int) {
            position = position move steps
            score += position
        }

        fun isWon(): Boolean = score >= 1000
    }

    class Die {
        var rolls: Int = 0
        private var value = 1
        fun roll(): Int {
            val result = when (value) {
                100 -> 103
                99 -> 200
                else -> 3 * value + 3
            }
            rolls += 3
            value = (rolls % 100) + 1
            return result
        }
    }

    private fun solvePart2(initialP1: Int, initialP2: Int): Long {
        val p1Results = countWinningTurns(initialP1)
        val p2Results = countWinningTurns(initialP2)

        val (winnerResults, looserPosition) = if (p1Results.values.sum() >= p2Results.values.sum()) p1Results to initialP2 else p2Results to initialP1

        return winnerResults.toList()
            .sumOf { (wonOnTurn, totalWinnerRolls) ->
                totalWinnerRolls * countLoosingTurns(looserPosition, 0, wonOnTurn - 1)
            }
    }

    // count of rolls combination to reach specified sum
    private val combinationsCountPerRolledSum = listOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)

    private fun countWinningTurns(
        position: Int,
        score: Int = 0,
        turnNum: Int = 0,
        totalDieRolls: Long = 1,
        result: MutableMap<Int, Long> = mutableMapOf()
    ): MutableMap<Int, Long> {
        if (score >= 21) {
            result[turnNum] = result.getOrDefault(turnNum, 0) + totalDieRolls
            return result
        }
        combinationsCountPerRolledSum.forEach { (rolledSum, countOfCombinations) ->
            val nextPos = position move rolledSum
            countWinningTurns(nextPos, score + nextPos, turnNum + 1, countOfCombinations * totalDieRolls, result)
        }
        return result
    }

    private fun countLoosingTurns(position: Int, score: Int, turnsAvailable: Int): Long {
        return when {
            score >= 21 -> 0
            turnsAvailable == 0 -> 1
            else -> combinationsCountPerRolledSum.sumOf { (rolledSum, countOfCombinations) ->
                val nextPos = position move rolledSum
                countOfCombinations * countLoosingTurns(nextPos, score + nextPos, turnsAvailable - 1)
            }
        }
    }

    private fun String.parse(): Pair<Int, Int> {
        return lines().map { l -> "\\d+".toRegex().findAll(l).last().value.toInt() }.let { (p1, p2) -> p1 to p2 }
    }
}

fun main() {
    DiracDice().runAll()
}