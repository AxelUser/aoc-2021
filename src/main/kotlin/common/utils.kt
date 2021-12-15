package common

fun Array<IntArray>.getAdjacent4(point: Pair<Int, Int>, scale: Int = 1): Sequence<Pair<Int, Int>> {
    val (i, j) = point
    val length = this@getAdjacent4.count() * scale
    val width = this@getAdjacent4[0].count() * scale
    return sequence {
        if (i > 0) yield(Pair(i - 1, j)) // top
        if (i < (length - 1)) yield(Pair(i + 1, j)) // bottom
        if (j > 0) yield(Pair(i, j - 1)) // left
        if (j < (width - 1)) yield(Pair(i, j + 1)) // right
    }
}

fun Array<IntArray>.getAdjacent8(point: Pair<Int, Int>): Sequence<Pair<Int, Int>> {
    val (i, j) = point
    val length = this@getAdjacent8.count()
    val width = this@getAdjacent8[i].count()
    return sequence {
        if (i > 0) {
            yield(Pair(i - 1, j)) // top
            if (j > 0) yield(Pair(i - 1, j - 1)) // top-left
            if (j < (width - 1)) yield(Pair(i - 1, j + 1)) // top-right
        }
        if (i < (length - 1)) {
            yield(Pair(i + 1, j)) // bottom
            if (j > 0) yield(Pair(i + 1, j - 1)) // bottom-left
            if (j < (width - 1)) yield(Pair(i + 1, j + 1)) // bottom-right
        }

        if (j > 0) yield(Pair(i, j - 1)) // left
        if (j < (width - 1)) yield(Pair(i, j + 1)) // right
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun String.parseMatrix(): Array<IntArray> {
    return this.lines().map { l -> l.map { c -> c.digitToInt() }.toIntArray() }.toTypedArray()
}