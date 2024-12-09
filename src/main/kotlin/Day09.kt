import java.io.File

fun main() {
    Day09().puzzle1()
    Day09().puzzle2()
}

class Day09 {
    private val input = File("inputs/day09.txt").readText().trim()
    //private val input = "2333133121414131402"
    fun puzzle1() {
        var id = 0
        val out = input.flatMapIndexed { idx, c ->
            val freeOrId = if (idx % 2 == 0) id++ else -1
            (1..c.digitToInt()).map { freeOrId }
        }.toMutableList()

        var eof = out.lastIndex
        for (i in out.indices) {
            if (i >= eof) break
            if (out[i] < 0) { // fill up free space (-1 is free)
                out[i] = out[eof]
                out[eof] = -1
                while (out[eof] < 0) eof--
            }
        }

        out.mapIndexed { idx, v -> if (v >= 0) idx * v.toLong() else 0 }.sum().printAnswer()
    }

    fun puzzle2() {
        data class Block(val pos:Int, val digit: Int, val fileId: Int)
        val temp = ArrayDeque<Block>()
        val space = ArrayDeque<Block>()

        var fileId = 0
        var pos = 0
        val out = input.flatMapIndexed { idx, c ->
            if (idx % 2 == 0)   temp.add(Block(pos, c.digitToInt(), fileId))
            else                space.add(Block(pos, c.digitToInt(), -1))

            pos += c.digitToInt()
            val freeOrId = if (idx % 2 == 0) fileId++ else -1

            (1..c.digitToInt()).map { freeOrId }
        }.toMutableList()

        temp.reversed().forEach { tempBlock ->
            space.forEachIndexed { spaceIdx, spaceBlock ->
                if (spaceBlock.pos < tempBlock.pos && tempBlock.digit <= spaceBlock.digit) {
                    (0 ..< tempBlock.digit).forEach { i ->
                        out[tempBlock.pos+i] = -1
                        out[spaceBlock.pos+i] = tempBlock.fileId
                    }
                    space[spaceIdx] = Block(spaceBlock.pos+tempBlock.digit, spaceBlock.digit-tempBlock.digit, -1)
                    return@forEach
                }
            }
        }
        out.mapIndexed { idx, v -> if (v >= 0) idx * v.toLong() else 0 }.sum().printAnswer()
    }
}