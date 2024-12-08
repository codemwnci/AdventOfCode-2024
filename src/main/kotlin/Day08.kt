import java.io.File
import java.util.HashMap

fun main() {
    Day08().puzzle1()
    Day08().puzzle2()
}

class Day08 {
    private val file = File("inputs/day08.txt")
    private val map = file.readLines().map { it.toList() }

    data class Point(val x:Int, val y:Int) {
        fun add(other: Point) = Point(x+other.x, y+other.y)
        fun subtract(other: Point) = Point(x-other.x, y-other.y)
    }

    private fun List<List<Char>>.isInBounds(p: Point) = p.y in this.indices && p.x in this[0].indices

    fun puzzle1() {
        // first, group by all the different frequencies (letters)
        val antennas = HashMap<Char, MutableList<Point>>()
        map.forEachIndexed { y, rows -> rows.forEachIndexed { x, c ->
            if (c != '.') { antennas.getOrPut(c) { mutableListOf() }.add(Point(x, y)) }
        } }

        // then get the different combinations of 2 for each frequency, to find the different pairings
        // finally add two points either side of those pairs, and make sure they don't go outside the boundary
        antennas.values.flatMap {
            it.combinations(2).flatMap {
                val dist = it[0].subtract(it[1])
                val a = it[0].add(dist)
                val b = it[1].subtract(dist)

                mutableListOf<Point>().let {
                    if (map.isInBounds(a)) it.add(a)
                    if (map.isInBounds(b)) it.add(b)
                    it.toList()
                }
            }
        }.distinct().size.printAnswer("1")
    }

    fun puzzle2() {
        // first, group by all the different frequencies (letters)
        val antennas = HashMap<Char, MutableList<Point>>()
        map.forEachIndexed { y, rows -> rows.forEachIndexed { x, c ->
            if (c != '.') { antennas.getOrPut(c) { mutableListOf() }.add(Point(x, y)) }
        } }

        // then get the different combinations of 2 for each frequency, to find the different pairings
        // finally add two points either side of those pairs, and make sure they don't go outside the boundary
        // unlike part1, keep increasing the distance until we go out of bounds
        antennas.values.flatMap {
            it.combinations(2).flatMap {
                val dist = it[0].subtract(it[1])
                val list = mutableListOf<Point>()

                var a = it[0]
                while (map.isInBounds(a)) {
                    list.add(a)
                    a = a.add(dist)
                }

                var b = it[1]
                while (map.isInBounds(b)) {
                    list.add(b)
                    b = b.subtract(dist)
                }

                list.toList()
            }
        }.distinct().size.printAnswer("2")
    }
}