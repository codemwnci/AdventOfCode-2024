import java.io.File

fun main() {
    Day11().puzzle1()
    Day11().puzzle2()
}

class Day11 {
    private val file = File("inputs/day11.txt")
    private val stones = file.readText().trim().split(" ").map { it.toLong() }

    fun puzzle1() {
        var res = stones.toList()
        repeat(25) {
            res = res.flatMap {
                val intAsString = it.toString()
                when {
                    it == 0L -> listOf(1)
                    intAsString.length % 2 == 0 -> {
                        val lhs = intAsString.take(intAsString.length / 2).toLong()
                        val rhs = intAsString.takeLast(intAsString.length / 2).toLong()
                        listOf(lhs, rhs)
                    }
                    else -> listOf(it * 2024)
                }
            }
        }
        res.size.printAnswer()
    }

    fun puzzle2() {
        // resisted recursion in part1, but clearly needed in part2 when caching became obviously needed
        // largely the same approach to part1 (getOrPut makes the code pretty elegant)
        val cache = HashMap<Pair<Long, Int>, Long>()
        fun solve(num: Long, depth: Int): Long {
            return cache.getOrPut(num to depth) {
                val intAsString = num.toString()
                when {
                    depth == 0 -> 1L
                    num == 0L -> solve(1, depth - 1)
                    intAsString.length % 2 == 0 -> {
                        val lhs = intAsString.take(intAsString.length / 2).toLong()
                        val rhs = intAsString.takeLast(intAsString.length / 2).toLong()
                        solve(lhs, depth - 1) + solve(rhs, depth - 1)
                    }
                    else -> solve(num * 2024, depth - 1)
                }
            }
        }
        stones.sumOf { solve(it, 75) }.printAnswer()
    }
}