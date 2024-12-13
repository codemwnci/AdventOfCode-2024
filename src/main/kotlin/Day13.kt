import java.io.File

fun main() {
    Day13().puzzle1()
    Day13().puzzle2()
}

class Day13 {
    private val file = File("inputs/day13.txt")
    data class XY(val x: Long, val y:Long) {
        fun add(other: XY) = XY(x+other.x, y+other.y)
    }
    private fun List<Long>.toXY() = XY(this[0], this[1])

    fun puzzle1() {
        val machines = file.readLines().splitOnBlank().map {
            it.map { it.substringAfter(": ").split(", ").map { it.drop(2).toLong() }.toXY() }
        }

        machines.mapNotNull { m ->
            (1..100).flatMap { a -> (1..100).mapNotNull { b ->
                if (m[0].x*a+m[1].x*b == m[2].x && m[0].y*a+m[1].y*b == m[2].y)
                    3 * a + b
                else null
            } }.minOrNull()
        }.sum().printAnswer()
    }

    fun puzzle2() {
        val machines = file.readLines().splitOnBlank().map {
            it.map { it.substringAfter(": ").split(", ").map { it.drop(2).toLong() }.toXY() }
        }

        machines.sumOf { m ->
            val A = m[0]
            val B = m[1]
            val t = m[2].add(XY(10000000000000, 10000000000000))
            val a = (t.x*B.y-t.y*B.x)/(B.y*A.x-B.x*A.y)
            val b = (t.x*A.y-t.y*A.x)/(A.y*B.x-B.y*A.x)

            if (A.x*a+B.x*b==t.x && A.y*a+B.y*b==t.y) 3*a+b else 0
        }.printAnswer()
    }
}