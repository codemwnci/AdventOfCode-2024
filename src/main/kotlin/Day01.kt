import java.io.File
import kotlin.math.abs

fun main() {
    Day01().puzzle1()
    Day01().puzzle2()
}

class Day01 {
    private val file = File("inputs/day01.txt")
    private val lists = file.readLines().map { line -> line.split("   ").map { it.toInt() }.toPair() }.unzip()
    private val left = lists.first.sorted()
    private val right = lists.second.sorted()

    fun puzzle1() {
        left.zip(right).sumOf { abs(it.second - it.first) }.printAnswer()
    }

    fun puzzle2() {
        //left.sumOf { first -> right.count { it == first } * first  }.printAnswer()
        val counts = right.groupingBy { it }.eachCount() // optimise by calculating counts once -- not massively needed, but saves a few millis
        left.sumOf { counts.getOrDefault(it, 0) * it }.printAnswer()
    }
}