import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    Day18().puzzle1()
    Day18().puzzle2()
}

class Day18 {
    private val file = File("inputs/day18.txt")
    private val corruptMemory = file.readLines().map { it.split(",").map { it.toInt() }.toXY() }
    private val dirs = listOf(XY(0,-1), XY(1, 0), XY(0, 1), XY(-1, 0))

    private fun List<Int>.toXY() = XY(this[0], this[1])
    data class XY(val x: Int, val y:Int) {
        fun move(other: XY) = XY(x+other.x, y+other.y)
        fun isInBound(end: XY) = (x in 0..end.x && y in 0..end.y)
        fun manhattan(end: XY): Int = abs(this.y - end.y) + abs(this.x - end.x)
    }

    private fun walkGrid(fallenBytes: List<XY>): Int {
        val start = XY(0,0)
        val end = XY(70,70)
        fun nextSteps(xy:XY): List<XY> = dirs.map { xy.move(it) }.mapNotNull { if (it.isInBound(end) && !fallenBytes.contains(it)) it else null }

        val queue = PriorityQueue<Pair<XY, Int>>(compareBy { it.second + it.first.manhattan(end) })
        val visited = mutableMapOf(start to 0)

        queue.add(start to 0)
        while (queue.isNotEmpty()) {
            val (current, dist) = queue.poll()
            if (current == end) return dist

            for (next in nextSteps(current).filter { !visited.containsKey(it) && it !in fallenBytes }) {
                val nextScore = visited[current]!! + 1
                if (nextScore < visited.getOrDefault(next, Int.MAX_VALUE)) {
                    visited[next] = nextScore
                    queue.add(next to nextScore)
                }
            }
        }
        return -1
    }

    fun puzzle1() {
        walkGrid(corruptMemory.take(1024)).printAnswer()
    }

    fun puzzle2() {
        (1024 until corruptMemory.size).forEach {
            if (walkGrid(corruptMemory.take(it)) == -1) {
                val (x, y) = corruptMemory[it-1]
                "$x,$y".printAnswer()
                return
            }
        }
    }
}