import java.io.File

fun main() {
    Day12().puzzles()
}

// Would have liked this one to be more functional, but it was getting too messy, so just went imperative.
// Also initially made the mistake that I tried to group by Char's not realising that a single Char may have
// multiple areas. The initial solution was nicely functional, but failed to account for the multi areas with the same
// Char.

class Day12 {
    private val file = File("inputs/day12.txt")
    private val grid = file.readLines().map { it.toList() }
    data class Point(val x:Int, val y:Int) {
        fun add(other: Point) = Point(x+other.x, y+other.y)
    }
    private val dirs = listOf(Point(0,-1), Point(1, 0), Point(0, 1), Point(-1, 0))
    private fun List<List<Char>>.at(p:Point) = this[p.y][p.x]
    private fun List<List<Char>>.isInBounds(p: Point) = p.y in this.indices && p.x in this[0].indices

    fun puzzles() {
        var part1 = 0
        var part2 = 0

        val seen = mutableSetOf<Point>()
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                val p = Point(x, y)
                if (p in seen) continue
                val dfs = ArrayDeque<Point>().also { it.add(p) }
                var area = 0
                var perim = 0
                val perimSet = mutableMapOf<Point, MutableSet<Point>>()

                while (dfs.isNotEmpty()) {
                    val p1 = dfs.removeFirst()
                    if (p1 !in seen) {
                        seen.add(p1)
                        area++

                        dirs.forEach { d ->
                            val neighbour = p1.add(d)
                            if (grid.isInBounds(neighbour) && grid.at(p1) == grid.at(neighbour)) {
                                dfs.add(neighbour)
                            } else {
                                perim++
                                perimSet.getOrPut(d) { mutableSetOf() }.add(neighbour)
                            }
                        }
                    }
                }

                var sides = 0
                for ((_, vs) in perimSet) {
                    val seenPerim = mutableSetOf<Point>()
                    for (p1 in vs) {
                        if (p1 !in seenPerim) {
                            sides++
                            val perimQueue = ArrayDeque<Point>()
                            perimQueue.add(p1)
                            while (perimQueue.isNotEmpty()) {
                                val p2 = perimQueue.removeFirst()
                                if (p2 !in seenPerim) {
                                    seenPerim.add(p2)

                                    dirs.forEach { d ->
                                        val p3 = p2.add(d)
                                        if (p3 in vs) perimQueue.add(p3)
                                    }
                                }
                            }
                        }
                    }
                }

                part1 += area * perim
                part2 += area * sides
            }
        }

        part1.printAnswer("Part 1")
        part2.printAnswer("Part 2")
    }
}