import java.io.File

fun main() {
    Day10().puzzle1()
    Day10().puzzle2()
}

class Day10 {
    private val file = File("inputs/day10.txt")
    private val map = file.readLines().map { it.map { it.digitToInt() } }
    private val dirs = listOf(Point(0,-1), Point(1, 0), Point(0, 1), Point(-1, 0))

    data class Point(val x:Int, val y:Int) {
        fun add(other: Point) = Point(x+other.x, y+other.y)
    }

    private fun List<List<Int>>.heightAt(p:Point) = if (p.y in this.indices && p.x in this[0].indices) this[p.y][p.x] else null

    private fun nextSteps(point:Point): List<Point> =
        dirs.map { point.add(it) }.mapNotNull {
            val curr = map.heightAt(point)
            val nxt = map.heightAt(it)
            if (curr != null && nxt != null && curr+1 == nxt) it else null
        }

    private val allPossibleStarts = map.flatMapIndexed { y , row ->  row.mapIndexedNotNull { x, v ->  if (v == 0) Point(x, y) else null  } }
    private val allReachablePeaks = allPossibleStarts.map { start ->
        val paths = ArrayDeque<Point>().also { it.add(start) }
        val peaks = mutableListOf<Point>()
        while (paths.isNotEmpty()) {
            paths.pop()?.let {
                if (map.heightAt(it) == 9) peaks.add(it)
                else {
                    nextSteps(it).forEach { paths.push(it) }
                }
            }
        }
        peaks
    }

    fun puzzle1() {
        allReachablePeaks.sumOf { it.distinct().size }.printAnswer()
    }

    fun puzzle2() {
        allReachablePeaks.sumOf { it.size }.printAnswer()
    }
}