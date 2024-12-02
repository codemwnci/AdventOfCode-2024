import java.io.File

fun main() {
    Day02().puzzle1()
    Day02().puzzle2()
}

class Day02 {
    private val file = File("inputs/day02.txt")

    fun puzzle1() {
        val reports = file.readLines().map { it.split(" ").map { it.toLong() } }
        val allDec = reports.count { it.windowed(2, 1).all { it[0] - it[1] in 1..3 } }
        val allAcc = reports.count { it.windowed(2, 1).all { it[1] - it[0] in 1..3 } }

        println(allAcc + allDec)
    }

    private fun List<Long>.oneRemoved() = List(this.size) { idx -> this.filterIndexed { idx2, _ -> idx != idx2 } }

    fun puzzle2() {
        val reports = file.readLines().map { it.split(" ").map { it.toLong() } }
        // we can simply test all variations with one element removed (because those valid in part1 will be still valid with the first element removed
        val allDec = reports.count { it.oneRemoved().any { it.windowed(2, 1).none { it[0] - it[1] !in 1..3 } } }
        val allAcc = reports.count { it.oneRemoved().any { it.windowed(2, 1).none { it[1] - it[0] !in 1..3 } } }

        println(allAcc + allDec)
    }
}