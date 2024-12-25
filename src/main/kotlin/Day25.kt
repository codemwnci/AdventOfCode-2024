import java.io.File

fun main() {
    Day25().puzzle1()
}

class Day25 {
    private val file = File("inputs/day25.txt")

    fun getLock(schematic: List<String>): IntArray = (0 until 5).map { x -> schematic.indexOfFirst { it[x] == '.' } - 1 }.toIntArray()
    fun getKey(schematic: List<String>): IntArray = getLock(schematic.reversed())

    fun puzzle1() {
        val (locks, keys) = file.readLines().splitOnBlank().fold(listOf(mutableListOf<IntArray>(), mutableListOf<IntArray>())) { acc, schematic ->
            if (schematic[0]=="#####") { acc[0].add(getLock(schematic)) }
            else { acc[1].add(getKey(schematic)) }
            acc
        }

        locks.sumOf { lock -> keys.count { key ->
            (0 until 5).all { x -> lock[x] + key[x] < 6 }
        } }.printAnswer()
    }
}