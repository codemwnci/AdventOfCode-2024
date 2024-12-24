import java.io.File

fun main() {
    Day22().puzzle1()
    Day22().puzzle2()
}

class Day22 {
    private val file = File("inputs/day22.txt")
    private val initSecrets = file.readLines().map { it.toLong() }

    private val MASK: Long = 0xffffff
    private fun generatePrices(init: Long) = generateSequence(init) {
        var v = it xor (it shl 6) and MASK
        v = v xor (v shr 5) and MASK
        v xor (v shl 11) and MASK
    }

    fun puzzle1() {
        initSecrets.sumOf { generatePrices(it).drop(2000).first() }.printAnswer()
    }

    fun puzzle2() {
        val prices = mutableMapOf<List<Int>, Int>().withDefault { 0 }
        initSecrets.forEach { secret ->
            val secrets = generatePrices(secret).take(2000).map { it.toInt() % 10 }.toList()
            val deltas = secrets.zipWithNext { a, b ->  b - a }
            val seen = HashSet<List<Int>>()
            deltas.windowed(4).forEachIndexed { idx, ints -> if (seen.add(ints)) prices[ints] = prices.getValue(ints) + secrets[idx+4]}
        }
        prices.maxOf { it.value }.printAnswer()
    }
}