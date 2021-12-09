package solutions.day4

import solutions.BaseSolution

const val BOARD_SIZE = 5

class GiantSquid: BaseSolution() {
    override fun part1(input: String): String {
        return solve(input).first().toString()
    }

    private fun List<List<BingoCell>>.sumUnmarked(): Int {
        return this.flatten().filter { !it.isMarked }.sumBy { it.value }
    }

    data class BingoCell(val boardId: Int, val row: Int, val col: Int, var value: Int, val rowsCount: MutableList<Int>, val colsCount: MutableList<Int>){
        var isMarked = false

        fun mark(): Boolean {
            isMarked = true

            rowsCount[row]++
            colsCount[col]++

            return rowsCount[row] == BOARD_SIZE || colsCount[col] == BOARD_SIZE
        }

        override fun toString(): String {
            return if (isMarked) "[${value}]" else value.toString()
        }
    }

    private fun parse(input: String): Triple<List<Int>, List<List<List<BingoCell>>>, Map<Int, List<BingoCell>>> {
        val groups = input.split("${System.lineSeparator()}${System.lineSeparator()}")

        val rgx = "\\d+".toRegex()
        val seq = rgx.findAll(groups[0]).map { m -> m.value.toInt() }.toList()

        val map = seq.associateWith { mutableListOf<BingoCell>() }

        val boards = mutableListOf<List<List<BingoCell>>>()

        for ((boardNum, boardSection) in groups.drop(1).withIndex()) {
            val bingoBoard = List(BOARD_SIZE){ mutableListOf<BingoCell>() }
            boards.add(bingoBoard)

            val parsed = boardSection.lines().map{ row -> rgx.findAll(row).map { num ->  num.value.toInt() }.toList()}.toList()

            val rowsCount = MutableList(BOARD_SIZE){0}
            val colsCount = MutableList(BOARD_SIZE){0}

            for (row in 0 until BOARD_SIZE) {
                for (col in 0 until BOARD_SIZE) {
                    val cell = BingoCell(boardNum, row, col, parsed[row][col], rowsCount, colsCount)
                    bingoBoard[row].add(cell)

                    if (map.containsKey(cell.value)) {
                        map[cell.value]?.add(cell)
                    }
                }
            }
        }

        return Triple(seq, boards, map)
    }

    private fun solve(input: String): Sequence<Int> {
        val (seq, boards, map) = parse(input)
        val wonBoards = hashSetOf<Int>()

        return sequence {
            for (number in seq) {
                for (cell in map.getValue(number)) {
                    if(!wonBoards.contains(cell.boardId) && cell.mark()) {
                        wonBoards.add(cell.boardId)
                        yield (boards[cell.boardId].sumUnmarked() * number)
                        continue
                    }
                }
            }
        }
    }

    override fun part2(input: String): String {
        return solve(input).last().toString()
    }
}

fun main() {
    GiantSquid().runAll()
}