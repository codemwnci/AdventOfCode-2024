import java.io.File

fun main() {
    Day14().puzzle1()
    Day14().puzzle2()
}

class Day14 {
    private val file = File("inputs/day14.txt")
    private val robots = file.readLines().map {
        it.split(" ").map { it.drop(2).split(",").map { it.toLong() }.toXY() }
    }

    data class Bounds(val width: Int, val height:Int)
    private fun List<Long>.toXY() = XY(this[0], this[1])
    data class XY(val x: Long, val y:Long) {
        fun move(other: XY, bounds: Bounds): XY {
            val newX = x+other.x
            val newY = y+other.y
            return XY(
                if (newX<0) newX+bounds.width else if (newX>=bounds.width) newX-bounds.width else newX,
                if (newY<0) newY+bounds.height else if (newY>=bounds.height) newY-bounds.height else newY
            )
        }
    }

    private val w = 101
    private val h = 103

    fun puzzle1() {
        val robotsAfterMove = (0 until 100).fold(robots) { list, _ ->
            list.map {
                val (p, v) = it
                listOf(p.move(v, Bounds(w, h)), v)
            }
        }

        val quadrants = listOf((0 until w/2) to (0 until h/2), (w/2+1 until w) to (0 until h/2),
                               (0 until w/2) to (h/2+1 until h), (w/2+1 until w) to (h/2+1 until h))

        quadrants.map { q ->
            robotsAfterMove.count { r -> r.first().x in q.first && r.first().y in q.second }
        }
        .reduce { acc, i -> acc*i }.printAnswer()
    }

    // Based on comments on Reddit, it appears that if all robots are in a unique position, the Xmas tree appears
    // so simply keep moving the robots until all are unique (i.e. distinct count of positions == number of robots)
    fun puzzle2() {
        (1 .. 100_000).fold(robots) { list, t ->
            list.map {
                val (p, v) = it
                listOf(p.move(v, Bounds(w, h)), v)
            }.also {
                if (it.count() == it.distinctBy{ it[0] }.count()) {
                    t.printAnswer()
                    return
                }
            }
        }
    }
}