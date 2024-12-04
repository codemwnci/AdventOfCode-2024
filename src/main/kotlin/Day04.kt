import java.io.File

fun main() {
    Day04().puzzle1()
    Day04().puzzle2()
}

class Day04 {
    private val file = File("inputs/day04.txt")
    private val wordMap = file.readLines().map { it.map { it } }

    private fun List<List<Char>>.searchWord(x:Int, y:Int, word: String): List<String> {
        return (-1 .. 1).flatMap { yStep -> (-1 .. 1).mapNotNull { xStep ->
            if (xStep==0 && yStep==0) null
            else searchOutward(x, y, word.length, yStep, xStep)
        }}.filter { it == word }
    }
    private fun List<List<Char>>.searchOutward(x:Int, y:Int, length: Int, yStep: Int, xStep: Int) =
        (0..< length).map { i -> if (y+yStep*i in this.indices && x+xStep*i in this[0].indices) this[y+yStep*i][x+xStep*i] else ' ' }.joinToString("")


    fun puzzle1() {
        val doSearch = wordMap.flatMapIndexed { y, chars -> chars.flatMapIndexed { x, c ->
            wordMap.searchWord(x, y, "XMAS")
        } }
        doSearch.size.printAnswer()
    }

    fun puzzle2() {
        val doSearch = wordMap.flatMapIndexed { y, chars -> chars.mapIndexedNotNull { x, c ->
            if (wordMap[y][x] == 'A') {
                if (y-1 in wordMap.indices && y+1 in wordMap.indices && x-1 in wordMap[0].indices && x+1 in wordMap[0].indices) {
                    val ms1 = ("" + wordMap[y-1][x+1] + wordMap[y+1][x-1])
                    val ms2 = ("" + wordMap[y+1][x+1] + wordMap[y-1][x-1])

                    if ((ms1 == "MS" || ms1 == "SM") && (ms2 == "MS" || ms2 == "SM")) true
                    else null
                }
                else null
            }
            else null
        } }
        doSearch.size.printAnswer()
    }
}