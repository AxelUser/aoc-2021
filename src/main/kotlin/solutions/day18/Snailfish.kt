package solutions.day18

import solutions.BaseSolution
import java.util.*
import kotlin.math.roundToInt

class Snailfish: BaseSolution() {
    override fun part1(input: String): Any {
        return input.parseBrackets().reduce{ a, b -> (a + b).reduce() }.magnitude()
    }

    override fun part2(input: String): Any {
        return input.lines().let {
            sequence {
                for (first in it) {
                    for(second in it) {
                        if (first != second) {
                            val res = (first.parseBracket() + second.parseBracket()).reduce()
                            yield(res)
                        }
                    }
                }
            }.maxOf { it.magnitude() }
        }
    }

    private interface Node: Cloneable {
        var parent: Bracket?
        fun magnitude(): Int
        fun addToLeftmostLeaf(value: Int, from: Node?)
        fun addToRightmostLeaf(value: Int, from: Node?)

        fun substituteWith(substitution: Node) {
            when {
                this === this.parent?.left -> this.parent?.left = substitution
                this === this.parent?.right -> this.parent?.right = substitution
            }
            this.parent = null
        }
    }

    private data class Number(var value: Int, override var parent: Bracket?): Node {
        override fun magnitude(): Int = value

        override fun toString(): String = value.toString(10)

        override fun addToLeftmostLeaf(value: Int, from: Node?) {
            this.value += value
        }

        override fun addToRightmostLeaf(value: Int, from: Node?) {
            this.value += value
        }
    }

    private data class Bracket(var left: Node? = null, var right: Node? = null, override var parent: Bracket? = null): Node {
        fun append(node: Node) {
            when {
                left == null -> left = node
                right == null -> right = node
                else -> error("left and right nodes are set")
            }
        }

        override fun magnitude(): Int = 3 * left!!.magnitude() + 2 * right!!.magnitude()

        override fun toString(): String = "[${left},${right}]"

        operator fun plus(other: Node): Bracket {
            val new = Bracket(this, other)
            this.parent = new
            other.parent = new
            return new
        }

        override fun addToLeftmostLeaf(value: Int, from: Node?) {
            if (parent == null && from === left) { // root from left child
                return
            }

            if (from === left) { // not root from left child
                return parent!!.addToLeftmostLeaf(value, this)
            }

            if (from === right) {
                return left!!.addToLeftmostLeaf(value, null)
            }

            return right!!.addToLeftmostLeaf(value, null)
        }

        override fun addToRightmostLeaf(value: Int, from: Node?) {
            if (parent == null && from === right) { // root from right child
                return
            }

            if (from === right) { // not root from right child
                return parent!!.addToRightmostLeaf(value, this)
            }

            if (from === left) {
                return right!!.addToRightmostLeaf(value, null)
            }

            return left!!.addToRightmostLeaf(value, null)
        }

        fun reduce(): Bracket {
            var current = this
            while (true) {
                val exploded = current.explode()
                if(exploded != null) {
                    current = exploded
                    continue
                }
                val split = current.split()
                if(split != null) {
                    current = split
                    continue
                }

                return current
            }
        }

        fun explode(): Bracket? {
            val stack = Stack<Pair<Int, Node>>()
            stack.push(Pair(0, this))
            while (stack.isNotEmpty()) {
                val (level, cur) = stack.pop()
                if (cur is Number) {
                    continue
                } else if (cur is Bracket) {
                    if (level == 4) {
                        with(cur.left as Number) {
                            cur.addToLeftmostLeaf(value, this)
                        }
                        with(cur.right as Number) {
                            cur.addToRightmostLeaf(value, this)
                        }

                        cur.substituteWith(Number(0, cur.parent))

                        return this
                    }
                    stack.push(Pair(level + 1, cur.right!!))
                    stack.push(Pair(level + 1, cur.left!!))
                }
            }

            return null
        }

        fun split(): Bracket? {
            val stack = Stack<Node>()
            stack.push(this)
            while (stack.isNotEmpty()) {
                val cur = stack.pop()
                if (cur is Number && cur.value >= 10) {
                    cur.substituteWith(Bracket(parent = cur.parent).apply {
                        left = Number(cur.value.floorDiv(2), this)
                        right = Number((cur.value / 2.0).roundToInt(), this)
                    })
                    return this
                } else if (cur is Bracket) {
                    stack.push(cur.right)
                    stack.push(cur.left)
                }
            }

            return null
        }
    }

    private fun String.parseBrackets(): List<Bracket> = lines().map { it.parseBracket() }

    private fun String.parseBracket(): Bracket {
        val stack = Stack<Node>()
        for (c in this) {
            when {
                c == '[' -> {
                    val parent = if(stack.isNotEmpty()) stack.peek() as Bracket else null
                    stack.push(Bracket(parent = parent))
                }
                c.isDigit() -> {
                    stack.push(Number(c.digitToInt(), stack.peek() as Bracket))
                }
                c == ',' || c == ']' -> {
                    val node = stack.pop()
                    (stack.peek() as Bracket).append(node)
                }
            }
        }
        return stack.single() as Bracket
    }

}

fun main() {
    Snailfish().runAll()
}