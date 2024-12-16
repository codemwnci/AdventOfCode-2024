import java.io.File
import java.util.PriorityQueue

fun main() {
    Day16().puzzle1()
    Day16().puzzle2()
}

class Day16 {
    private val file = File("inputs/day16.txt")
    private val grid = file.readLines().map { it.toList() }
    data class XY(val x: Int, val y:Int) {
        fun move(other: XY) = XY(x+other.x, y+other.y)
        fun mapPoint(map: Grid) = map[y][x]
    }
    private enum class Dir(val xy:XY) {
        EAST(XY(1, 0)), SOUTH(XY(0, 1)), WEST(XY(-1,0)), NORTH(XY(0, -1))
    }
    private fun rotate(curr: Dir) = when(curr) {
        Dir.EAST -> listOf(Dir.NORTH, Dir.SOUTH)
        Dir.SOUTH -> listOf(Dir.EAST, Dir.WEST)
        Dir.WEST -> listOf(Dir.SOUTH, Dir.NORTH)
        Dir.NORTH -> listOf(Dir.WEST, Dir.EAST)
    }

    private fun Grid.findFirstIndex(toFind: Char): XY? =
        this.flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                XY(x, y) to c
            }
        }.find { it.second == toFind }?.first

    // Has to be a BFS, rather than DFS
    // DFS will work without caching (but will run too slowly), but with Caching, DFS will cache answers that are
    // not the shortest path
    fun puzzle1() {
        val start = grid.findFirstIndex('S')
        val end = grid.findFirstIndex('E')

        val queue = PriorityQueue<Triple<XY, Dir, Int>>(compareBy { it.third })
        val visited = HashMap<Pair<XY, Dir>, Int>()

        var answer = 0
        queue.add(Triple(start!!, Dir.EAST, 0))
        while (queue.isNotEmpty()) {
            val (point, dir, score) = queue.poll()
            if (point == end) {
                answer = score
                break
            }
            if (visited.getOrDefault(point to dir, Int.MAX_VALUE) >= score) {
                visited[point to dir] = score
                val next = point.move(dir.xy)
                if (next.mapPoint(grid) != '#') queue.add(Triple(next, dir, score+1))
                rotate(dir).forEach { queue.add(Triple(point, it, score+1000)) }
            }
        }

        answer.printAnswer()
    }

    fun puzzle2() {
        val start = grid.findFirstIndex('S')
        val end = grid.findFirstIndex('E')

        val queue = PriorityQueue<Triple<List<XY>, Dir, Int>>(compareBy { it.third })
        val visited = HashMap<Pair<XY, Dir>, Int>()
        var min = Int.MAX_VALUE
        val best = HashSet<XY>()

        var answer = 0
        queue.add(Triple(listOf(start!!), Dir.EAST, 0))

        while (queue.isNotEmpty()) {
            val (points, dir, score) = queue.poll()

            if (points.last() == end) {
                if (score <= min) min = score
                else {
                    answer = best.size
                    break
                }
                best.addAll(points)
            }

            if (visited.getOrDefault(points.last() to dir, Int.MAX_VALUE) >= score) {
                visited[points.last() to dir] = score
                val next = points.last().move(dir.xy)
                if (next.mapPoint(grid) != '#') queue.add(Triple(points + next, dir, score+1))
                rotate(dir).forEach { queue.add(Triple(points, it, score+1000)) }
            }
        }
        answer.printAnswer()
    }
}