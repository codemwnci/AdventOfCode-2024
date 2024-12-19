import java.io.File

fun main() {
    Day19().puzzle1()
    Day19().puzzle2()
}

class Day19 {
    private val file = File("inputs/day19.txt")
    private val p = file.readLines().first().split(", ")
    private val d = file.readLines().drop(2)

    private val cache = HashMap<String, Long>()
    private fun permutations(design: String, patterns: List<String>): Long =
        if (design.isEmpty()) 1L
        else cache.getOrPut(design) {
            patterns.sumOf { if (design.startsWith(it)) permutations (design.drop(it.length), patterns) else 0L }
        }

    fun puzzle1() { d.count { permutations(it, p) >= 1 }.printAnswer() }
    fun puzzle2() { d.sumOf { permutations(it, p) }.printAnswer() }
}