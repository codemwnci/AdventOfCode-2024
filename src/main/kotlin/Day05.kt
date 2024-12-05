import java.io.File

fun main() {
    Day05().puzzle1()
    Day05().puzzle2()
}

class Day05 {
    private val file = File("inputs/day05.txt")
    private val parts = file.readLines().split { it.trim() == "" }
    private val rules = parts[0].map { it.split("|").map { it.toInt() } }
    private val manuals = parts[1].map { it.split(",").map { it.toInt() } }

    private fun isWellOrdered(manual: List<Int>) = manual.all { page ->
        val rulesForPage = rules.filter { it[0] == page }.map { it[1] } // just get the page to compare is not before
        rulesForPage.none { rule -> manual.indexOf(rule) != -1 && manual.indexOf(page) > manual.indexOf(rule) }
    }

    fun puzzle1() {
        manuals.filter { manual -> isWellOrdered(manual) }.sumOf { it[it.size/2] }.printAnswer()
    }

    fun puzzle2() {
        fun firstFailedRule(manual: List<Int>) = manual.mapNotNull { page ->
            val rulesForPage = rules.filter { it[0] == page }.map { it[1] } // just get the page to compare is not before
            val failed = rulesForPage.firstOrNull { rule -> manual.indexOf(rule) != -1 && manual.indexOf(page) > manual.indexOf(rule) }
            if (failed != null) page to failed else null
        }.first()

        manuals.filterNot { manual -> isWellOrdered(manual) }.map { manual ->
            var retry = manual
            while(!isWellOrdered(retry)) { // loop, moving all out of sequence numbers until it is well-ordered
                val failed = firstFailedRule(retry)
                retry = retry.toMutableList().apply {
                    add(retry.indexOf(failed.second), removeAt(retry.indexOf(failed.first)))
                }.toList()
            }
            retry
        }
        .sumOf { it[it.size/2]  }.printAnswer()
    }
}