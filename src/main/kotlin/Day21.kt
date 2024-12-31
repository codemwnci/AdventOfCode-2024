import java.io.File
import kotlin.math.abs

fun main() {
    Day21().puzzle1()
    Day21().puzzle2()
}

// Needed some inspiration for today's problem (maybe I can blame Flu induced mind-fog?)
// Found a good solution from Roman Elizarov (https://github.com/elizarov/AdventOfCode2024/blob/main/src/Day21_1.kt)
// Reworked it into a style that I found more aligned to my typical approach.

class Day21 {
    private val file = File("inputs/day21.txt")
    private data class XY(val x: Int, val y: Int)

    private val numPad = listOf("789".toList(), "456".toList(), "123".toList(), " 0A".toList())
    private val dirPad = listOf(" ^A".toList(), "<v>".toList())

    private fun List<List<Char>>.xyOf(c: Char): XY = this.flatMapIndexed { y, row -> row.mapIndexedNotNull { x, ch -> if (ch==c) XY(x, y) else null } }.single()
    private fun buildButtonPresses(dist: Int, charPos: Char, charNeg: Char) = (if (dist > 0) charPos else charNeg).toString().repeat(abs(dist))

    fun puzzle1() {
        val pads = listOf(numPad, dirPad, dirPad)
        fun shortestSequence(code: String, padIdx: Int = 0): Int {
            if (padIdx == pads.size) return code.length

            val p = pads[padIdx]
            val blank = p.xyOf(' ')
            var len = 0
            var (sx, sy) = p.xyOf('A')
            for (num in code) {
                val xy = p.xyOf(num)
                val vert = buildButtonPresses(xy.y - sy, 'v', '^')
                val horz = buildButtonPresses(xy.x - sx, '>', '<')

                len += minOf(
                    if (XY(sx, xy.y) != blank) shortestSequence(vert+horz+"A", padIdx+1) else Int.MAX_VALUE,
                    if (XY(xy.x, sy) != blank) shortestSequence(horz+vert+"A", padIdx+1) else Int.MAX_VALUE
                )
                sx = xy.x
                sy = xy.y
            }
            return len
        }

        file.readLines().sumOf { shortestSequence(it) * it.take(3).toInt() }.printAnswer()
    }

    fun puzzle2() {
        val pads = listOf(numPad) + List(25) { dirPad }
        val caches = Array(pads.size) { HashMap<String, Long> () }
        fun shortestSequence(code: String, padIdx: Int = 0): Long {
            if (padIdx == pads.size) return code.length.toLong()

            return caches[padIdx].getOrPut(code) {
                val p = pads[padIdx]
                val blank = p.xyOf(' ')
                var len = 0L
                var (sx, sy) = p.xyOf('A')
                for (num in code) {
                    val xy = p.xyOf(num)
                    val vert = buildButtonPresses(xy.y - sy, 'v', '^')
                    val horz = buildButtonPresses(xy.x - sx, '>', '<')
                    len += minOf(
                        if (XY(sx, xy.y) != blank) shortestSequence(vert+horz+"A", padIdx+1) else Long.MAX_VALUE,
                        if (XY(xy.x, sy) != blank) shortestSequence(horz+vert+"A", padIdx+1) else Long.MAX_VALUE
                    )
                    sx = xy.x
                    sy = xy.y
                }
                len
            }
        }

        file.readLines().sumOf { shortestSequence(it) * it.take(3).toInt() }.printAnswer()
    }
}