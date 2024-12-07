import java.io.File

fun main() {
    Day07().puzzle1()
    Day07().puzzle2()
}

class Day07 {
    private val file = File("inputs/day07.txt")
    private val nums = file.readLines().map { it.replace(":", "").split(" ").map { it.toLong() } } // just get a list of numbers, first is answer

    fun puzzle1() {
        nums.filter {
            val ans = it[0]
            val tail = it.drop(2)
            tail.fold(listOf(it[1])) { list, nextNum ->
                list.flatMap { listOf(it + nextNum, it * nextNum) }
            }
            .any { it == ans }
        }
        .sumOf {  it.first() }.printAnswer()
    }

    fun puzzle2() {
        nums.filter {
            val ans = it[0]
            val tail = it.drop(2)
            tail.fold(listOf(it[1])) { list, nextNum ->
                list.flatMap { listOf(it + nextNum, it * nextNum, "$it$nextNum".toLong()) }
            }
            .any { it == ans }
        }
        .sumOf {  it.first() }.printAnswer()
    }
}