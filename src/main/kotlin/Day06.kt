import java.io.File

fun main() {
    Day06().puzzle1()
    Day06().puzzle2()
}

class Day06 {
    private val file = File("inputs/day06.txt")
    private val map = file.readLines().map { it.toList() }
    private val start = map.indexOfFirst { it.contains('^') }.let { Point(map[it].indexOf('^'), it) }
    private val dirs = listOf(Point(0,-1), Point(1, 0), Point(0, 1), Point(-1, 0))

    data class Point(val x:Int, val y:Int) {
        fun add(other: Point) = Point(x+other.x, y+other.y)
        fun isObstruction(map: List<List<Char>>) = y in map.indices && x in map[0].indices && map[y][x] == '#'
    }

    private fun walkMap(mapToWalk: List<List<Char>>): Int {
        var pos = start
        var dir = 0
        val visited = mutableSetOf<Point>()
        var rotationsSinceLastNewVisit = 0
        while (pos.y in mapToWalk.indices && pos.x in mapToWalk[0].indices) {
            val next = pos.add(dirs[dir])
            if (next.isObstruction(mapToWalk)) {
                dir++
                dir %= 4
                rotationsSinceLastNewVisit++
                if (rotationsSinceLastNewVisit == 5) return -1
            }
            else {
                pos = next
                val newlyVisited = visited.add(pos)
                if (newlyVisited) rotationsSinceLastNewVisit = 0 // reset counter
            }
        }

        return (visited.size - 1)
    }

    fun puzzle1() {
        walkMap(map).printAnswer()
    }

    fun puzzle2() {
        val maps = map.flatMapIndexed { y: Int, rows: List<Char> ->
            rows.mapIndexedNotNull { x: Int, c: Char ->
               if (c != '.') null
               else { // create copy of map, with just this character changed
                    map.mapIndexed {yCopy, list ->  list.mapIndexed { xCopy, toCopy -> if (yCopy == y && xCopy == x) '#' else toCopy } }
               }
            }
        }

        maps.filter { walkMap(it) < 0 }.size.printAnswer()
    }
}