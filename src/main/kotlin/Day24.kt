import java.io.File

fun main() {
    Day24().puzzle1()
    Day24().puzzle2()
}

class Day24 {
    private val file = File("inputs/day24.txt")
    data class Gate(val inpA: String, val type: String, val inpB:String, val out:String)

    fun puzzle1() {
        val currents = file.readLines().splitOnBlank().first().fold(HashMap<String, Int>()) { acc, line ->
            line.split(": ").let { (wire, curr) ->
                acc.put(wire, curr.toInt())
            }
            acc
        }
        val gates = file.readLines().splitOnBlank().last().map {
            Regex("(\\w+) (\\w+) (\\w+) -> (\\w+)").matchEntire(it)!!.destructured.let { (inA, type, inB, out) ->
                Gate(inA, type, inB, out)
            }
        }

        val queue = ArrayDeque<Gate>().also { it.addAll(gates) }
        while (queue.isNotEmpty()) {
            // loop through each gate, trying to calculate the output. If it can't be calculated, because we don't
            // know an input value yet, then skip for now (put it to the back of the queue)
            val gate = queue.removeFirst()
            val inpA = currents[gate.inpA]
            val inpB = currents[gate.inpB]
            if (inpA != null && inpB != null) {
                currents[gate.out] = when(gate.type) {
                    "AND" -> inpA and inpB
                    "OR" -> inpA or inpB
                    "XOR" -> inpA xor inpB
                    else -> throw Error("Unrecognised Logic gate: ${gate.type}")
                }
            }
            else { queue.addLast(gate) }
        }

        // now find all z currents, then sort in order of highest to lowest and join as string (to generate the binary number),
        // and then convert to a long using Base 2 to get the decimal value
        currents.filterKeys { it.contains("z") }.toList().sortedByDescending { it.first.drop(1).take(2).toInt() }
            .joinToString("") { it.second.toString() }.toLong(2).printAnswer()
    }

    fun puzzle2() {

    }
}