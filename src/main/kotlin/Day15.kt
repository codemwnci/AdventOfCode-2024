import java.io.File

fun main() {
    Day15().puzzle1()
    Day15().puzzle2()
}

typealias Grid = List<List<Char>>
class Day15 {
    private val file = File("inputs/day15.txt")
    private val puzzle = file.readLines().splitOnBlank().let { (mapRaw, movesRaw) ->
        Puzzle(mapRaw.map { it.toList() },  movesRaw.flatMap { it.toList() })
    }

    data class Puzzle(val map: Grid, val moves: List<Char>)
    data class XY(val x: Int, val y:Int) {
        fun move(other: XY) = XY(x+other.x, y+other.y)
        fun mapPoint(map: Grid) = map[y][x]
        fun right() = XY(x+1, y)
        fun left() = XY(x-1, y)
    }

    private fun isVertical(dir:XY) = dir.y != 0
    private fun dir(c: Char) = when(c) {
        '^' -> XY(0, -1)
        '>' -> XY(1, 0)
        'v' -> XY(0, 1)
        '<' -> XY(-1, 0)
        else -> throw Exception("Unexpected move")
    }
    private fun Grid.pathToWall(from: XY, dir: XY): List<Char> {
        val c = from.mapPoint(this)
        return if (c == '#') listOf() else listOf(c) + pathToWall(from.move(dir), dir)
    }
    private fun Grid.findFirstIndex(toFind: Char): XY? =
        this.flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                XY(x, y) to c
            }
        }.find { it.second == toFind }?.first

    fun puzzle1() {
        puzzle.moves.fold(puzzle.map) { map, nextMove ->
            val robot = map.findFirstIndex('@')!!
            val dir = dir(nextMove)

            if (robot.move(dir).mapPoint(map) == '#') map // return map unchanged
            else {
                val toWall = map.pathToWall(robot, dir)
                if (!toWall.contains('.')) map // return map unchanged if there is no space to move into
                else {
                    // return a modified map of all boxes shifted
                    val newMap = map.map { it.toMutableList() }
                    toWall.takeWhile { it != '.' }.fold(robot.move(dir)) { pos, c ->
                        newMap[pos.y][pos.x] = c
                        pos.move(dir)
                    }
                    newMap[robot.y][robot.x] = '.'
                    newMap
                }
            }
        }.also {
            it.foldIndexed(0) { y, sum, row ->
                sum + row.foldIndexed(0) { x, xSum, c ->
                    if (c == 'O') xSum + (y * 100 + x) else xSum
                }
            }.printAnswer()
        }
    }

    fun puzzle2() {
        val map = puzzle.map.map {
            it.flatMap {
                when (it) {
                    '#' -> listOf('#','#')
                    'O' -> listOf('[',']')
                    '@' -> listOf('@','.')
                    '.' -> listOf('.','.')
                    else -> throw Exception("Unexpected Char in map")
                }
            }
        }.foldIndexed(mutableMapOf<XY, Char>()) { y, acc, row ->
            row.foldIndexed(acc) { x, acc, c ->
                acc[XY(x, y)] = c
                acc
            }
        }

        var robot = map.firstNotNullOf { if (it.value == '@') it.key else null }
        puzzle.moves.forEach { nextMove ->
            val dir = dir(nextMove)

            fun canMove(xy: XY): Boolean = when(map[xy]) {
                '[' -> if (isVertical(dir)) canMove(xy.move(dir)) && canMove(xy.move(dir).right()) else canMove(xy.move(dir))
                ']' -> if (isVertical(dir)) canMove(xy.move(dir)) && canMove(xy.move(dir).left()) else canMove(xy.move(dir))
                '#' -> false
                '.' -> true
                else -> throw Exception("Unexpected move 1, $xy, ${map[xy]}")
            }

            fun doMove(xy:XY, replaceWith:Char) {
                when(map[xy]) {
                    '[' -> if (isVertical(dir)) {
                            doMove(xy.move(dir), '[')
                            doMove(xy.move(dir).right(), ']')
                            map[xy] = replaceWith
                            map[xy.right()] = '.'
                        } else {
                            doMove(xy.move(dir), '[')
                            map[xy] = replaceWith
                        }
                    ']' -> if (isVertical(dir)) {
                            doMove(xy.move(dir), ']')
                            doMove(xy.move(dir).left(), '[')
                            map[xy] = replaceWith
                            map[xy.left()] = '.'
                        } else {
                            doMove(xy.move(dir), ']')
                            map[xy] = replaceWith
                        }
                    '.' -> map[xy] = replaceWith

                    else -> throw Exception("Unexpected move 2, $xy, ${map[xy]}")
                }
            }

            map[robot] = '.'
            val moved = robot.move(dir)
            if(canMove(moved)) {
                doMove(moved, '.')
                robot = robot.move(dir)
            }
        }

        map.filter { it.value == '[' }.map { it.key }.sumOf { it.y*100 + it.x }.printAnswer()
    }
}