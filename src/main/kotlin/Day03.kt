import java.io.File

fun main() {
    Day03().puzzle1()
    Day03().puzzle2()
}

class Day03 {
    private val instr = File("inputs/day03.txt").readText()

    fun puzzle1() {
        val items = Regex("""mul\((\d+),(\d+)\)""").findAll(instr)
        items.sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }.printAnswer()
    }

    data class Total(val amount: Int, val enabled: Boolean = true)
    fun puzzle2() {
        val items = Regex("""mul\((\d+),(\d+)\)|don't|do""").findAll(instr)
        items.fold(Total(0)) { acc, res ->
            if (res.groupValues[0].startsWith("do")) { // catch do/don't, and set whether it is enabled or not
                Total(acc.amount, res.groupValues[0]=="do")
            }
            else { // only add if currently enabled, otherwise skip
                if (acc.enabled) Total(acc.amount + res.groupValues[1].toInt()*res.groupValues[2].toInt(), acc.enabled)
                else acc
            }
        }.printAnswer()
    }
}