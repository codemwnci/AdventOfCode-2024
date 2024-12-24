import java.io.File
import org.jgrapht.graph.*
import org.jgrapht.alg.clique.BronKerboschCliqueFinder

fun main() {
    Day23().puzzle1()
    Day23().puzzle2()
}

class Day23 {
    private val file = File("inputs/day23.txt")

    fun puzzle1() {
        val adj = file.readLines().fold(mutableMapOf<String,MutableSet<String>>()) { acc, s ->
            val (a, b) = s.split("-")
            acc.getOrPut(a) { mutableSetOf() }.add(b)
            acc.getOrPut(b) { mutableSetOf() }.add(a)
            acc
        }

        var ans = 0
        for (a in adj.keys) {
            for (b in adj[a]!!) {
                if (a >= b) continue
                for (c in adj[a]!!.intersect(adj[b]!!)) {
                    if (b >= c) continue
                    if (listOf(a, b, c).any { it.startsWith("t") }) ans++
                }
            }
        }
        ans.printAnswer()
    }

    // Sold out and used JGraph when I saw others were using the BronKerbosch algorithm
    fun puzzle2() {
        val networks = file.readLines().fold(SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java)) { graph, s ->
            s.split("-").let { (a, b) ->
                graph.addVertex(a)
                graph.addVertex(b)
                graph.addEdge(a, b)
            }
            graph
        }
        BronKerboschCliqueFinder(networks).maxBy { it.size }.sorted().joinToString(",").printAnswer()
    }
}