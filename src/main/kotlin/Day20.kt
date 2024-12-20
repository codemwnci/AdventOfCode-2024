import java.io.File
import kotlin.math.abs

fun main() {
    Day20().puzzle1()
    Day20().puzzle2()
}

class Day20 {
    private val file = File("inputs/day20.txt")
    private val grid = file.readLines().foldIndexed(HashMap<XY, Char>()) { y, hmap, row ->
        row.forEachIndexed { x, c -> hmap.put(XY(x,y), c) }
        hmap
    }
    val start = grid.firstNotNullOf { if (it.value=='S') it.key else null }
    val end = grid.firstNotNullOf { if (it.value=='E') it.key else null }
    val bounds = grid.maxBy { it.key.x + it.key.y }.key

    data class XY(val x: Int, val y:Int) {
        fun move(other: XY) = XY(x+other.x, y+other.y)
        fun isInBound(end: XY) = (x in 0..end.x && y in 0..end.y)
        fun manhattan(end: XY): Int = abs(this.y - end.y) + abs(this.x - end.x)
    }
    private val dirs = listOf(XY(0,-1), XY(1, 0), XY(0, 1), XY(-1, 0))

    private val path = mutableSetOf(start).let { seen ->
        generateSequence(start) { current -> dirs.map { dir -> current.move(dir) }.first { it.isInBound(bounds) && grid[it] != '#' && seen.add(it) } }.takeWhile { it != end }.toList() + end
    }

    val PICOS_SAVED = 100

    private fun cheat(xy: XY, picos: Int): Int {
        return (path.indexOf(xy) + PICOS_SAVED + 2..< path.size).count { endIdx ->
            val cheatEnd = path[endIdx]
            xy.manhattan(cheatEnd) <= picos && endIdx - path.indexOf(xy) - xy.manhattan(cheatEnd) >= PICOS_SAVED
        }
    }

    fun puzzle1() { path.sumOf { cheat(it, 2) }.printAnswer() }
    fun puzzle2() { path.sumOf { cheat(it, 20) }.printAnswer() }
}