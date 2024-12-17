import kotlinx.coroutines.*
import java.io.File
import kotlin.math.pow

fun main() {
    Day17().puzzle1()
    Day17().puzzle2()
}

class Day17 {
    private val file = File("inputs/day17.txt")

    private fun runComp(program: List<Int>, register: List<Long>): MutableList<Int> {
        var a = register[0]
        var b = register[1]
        var c = register[2]
        fun comboOperand(combo: Int):Long = when(combo) {
            in 0..3 -> combo.toLong()
            4 -> a
            5 -> b
            6 -> c
            7 -> throw Exception("unexpected")
            else -> throw Exception("unexpected")
        }

        var pointer = 0
        val out = mutableListOf<Int>()
        while (pointer < program.size - 1) {
            var jump = 2
            val instr = program[pointer]
            val opcode = program[pointer+1]
            when (instr) {
                0 -> a = a / 2.0.pow(comboOperand(opcode).toDouble()).toLong()
                1 -> b = b xor opcode.toLong()
                2 -> b = comboOperand(opcode) % 8
                3 -> if (a != 0L) {
                    jump = 0
                    pointer = opcode
                }
                4 -> b = b xor c
                5 -> out.add((comboOperand(opcode) % 8).toInt())
                6 -> b = a / 2.0.pow(comboOperand(opcode).toDouble()).toLong()
                7 -> c = a / 2.0.pow(comboOperand(opcode).toDouble()).toLong()
            }
            pointer += jump
        }
        return out
    }

    fun puzzle1() {
        val (register, program) = file.readLines().splitOnBlank().let {
            val reg = it[0].map { it.substringAfter(": ").toLong() }
            val prog = it[1][0].substringAfter(": ").split(",").map { it.toInt() }
            reg.toMutableList() to prog
        }

        runComp(program, register).joinToString(",").printAnswer()
    }

    // Brute for wasn't going to work, and parallel maps weren't making a difference.
    // Found a helpful approach --> https://pastebin.com/raw/7DC2mf9g. The following reasoning, taken from the pastebin
//This program loops while a > 0, dividing a by 8 every iteration.  Each iteration outputs one value, solely
//derived from the value of a % 8 at the beginning of the iteration.  Thus each iteration is completely independent
//and can be solved for in isolation.  The solution below starts by finding the value of a that outputs solely the
//last value of the program by starting with a=0 and incrementing by one each attempt.  The value of a that finds the last
//instruction is multiplied by 8, then used as the starting point for a search to find the starting value of a
//which will output the last two instructions.  This process is repeated until all instructions are output.
    fun puzzle2() {
        fun findAMatchingOutput(program: List<Int>, target: List<Int>): Long {
            var aStart = if (target.size == 1) {
                0
            } else {
                8L * findAMatchingOutput(program, target.subList(1, target.size))
            }

            while( runComp(program, listOf(aStart, 0, 0)) != target) {
                aStart++
            }
            return aStart
        }

        val program = file.readLines().splitOnBlank().let { it[1][0].substringAfter(": ").split(",").map { it.toInt() } }
        findAMatchingOutput(program, program).printAnswer()
    }
}