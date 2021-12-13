package solutions.day12

import solutions.BaseSolution
import java.util.*

class PassagePathing: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parseTable().countPaths()
    }

    override fun part2(input: String): Any {
        return input.parseTable().countPaths(canReuseInitially = true)
    }

    private fun Map<String, List<String>>.countPaths(start: String = "start", canReuseInitially: Boolean = false): Int {
        val stack = Stack<Triple<String, Set<String>, Boolean>>()
        var foundPaths = 0
        stack.addAll(getOrDefault(start, emptyList()).map { Triple(it, emptySet(), canReuseInitially) })

        while (stack.isNotEmpty()) {
            val (curNode, visited, reuse) = stack.pop()
            if (curNode == "end") {
                foundPaths++
                continue
            }

            val (visitedAfter, canReuse) = when {
                curNode[0].isUpperCase() -> Pair(visited, reuse) // big cave
                !visited.contains(curNode) -> Pair(visited.plus(curNode), reuse) // new small cave
                reuse -> Pair(visited.plus(curNode), false) // reuse small cave
                else -> continue // already reused small cave
            }

            stack.addAll(getOrDefault(curNode, emptyList()).map { Triple(it, visitedAfter, canReuse) })
        }

        return foundPaths
    }

    private fun String.parseTable(): Map<String, List<String>> {
        val ways = this.lines()
            .map { it.split('-')
                .let { (s, f) -> arrayOf(s to f, f to s) }
                .fold(listOf<Pair<String, String>>()){list, (s ,f) -> if (f != "start" && s != "end") list.plus(s to f) else list}
            }.flatten()
        return ways.groupingBy { it.first }.fold(listOf()){ caves, (_, cave) -> caves.plus(cave) }
    }
}

fun main() {
    PassagePathing().runAll()
}