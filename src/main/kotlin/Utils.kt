import java.io.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayDeque

// Helper functions to make ArrayDeque look more like a Stack
fun <T> ArrayDeque<T>.push(element: T) = addLast(element)
fun <T> ArrayDeque<T>.pop() = removeLastOrNull()
fun <T> ArrayDeque<T>.peek() = lastOrNull()

fun <T> priorityQueueOf(vararg args: T): PriorityQueue<T> = PriorityQueue<T>().also { it.addAll(args) }

fun <T> List<T>.subListFrom(from: Int) = this.subList(from, this.lastIndex)

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> = fold(mutableListOf(mutableListOf<T>())) { acc, t ->
    if (predicate(t)) acc.add(mutableListOf())
    else acc.last().add(t)
    acc
}

fun List<String>.splitOnBlank(): List<List<String>> = this.split { it.isBlank() }

fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
    0 -> listOf(listOf())
    else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
}
operator fun <T> List<T>.component6(): T { return this[5] }

fun Any.printAnswer() = println("Answer: ${this}")
fun Any.printAnswer(part: String) = println("Answer $part: $this")

fun lcm(values: Collection<Number>) = values.fold(1L) { acc, i -> lcm(acc, i.toLong()) }
private fun lcm(a:Long, b:Long): Long {
    val larger = if (a > b) a else b
    var lcm = larger
    while (!(lcm % a == 0L && lcm % b == 0L)) {
        lcm += larger
    }
    return lcm
}

fun <T> List<T>.toPair(): Pair<T, T> = if (size == 2) Pair(this[0], this[1]) else throw IllegalArgumentException("List must contain exactly 2 elements")

fun main(args: Array<String>) {

    println("Generating Today's Kotlin File and downloading Input Data")

    val cookie = args.getOrNull(0)

    val template = File("src/main/kotlin/Template.kt").readText()
    val day = LocalDate.now().dayOfMonth.toString().padStart(2, '0')
    val todaysCode = File("src/main/kotlin/Day${day}.kt")
    val todaysInput = File("inputs/day${day}.txt")

    // create file if it doesn't exist
    if (todaysCode.createNewFile()) {
        todaysCode.writeText(template.replace("XX", day))
        println("Kotlin File Generated --> ${todaysCode.canonicalPath}")
    }

    if (cookie != null && todaysInput.createNewFile()) {
        val http = java.net.URL("https://adventofcode.com/2024/day/${LocalDate.now().dayOfMonth}/input").openConnection()
        http.addRequestProperty("Cookie", "session=$cookie")
        todaysInput.writeText(InputStreamReader(http.getInputStream()).readText())
        println("Puzzle Input Downloaded --> ${todaysInput.canonicalPath}")
    }
    else {
        if (cookie == null) {
            println("Puzzle input NOT downloaded - no Cookie present")
        }
    }
}