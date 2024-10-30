import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val vertexCount = 1000000
        val graph = Graph(vertexCount)
        val parallelGraph = ParallelGraph(vertexCount)
        val dijkstraParallel = DijkstraParallel()
        val dijkstraClassic = DijkstraClassic ()
        val dijkstraThreads = ParallelDijkstraThreads ()

        for (i in 0 until vertexCount - 1) {
            graph.addEdge(i, i + 1, (1..10).random())
            graph.addEdge(i, (i + (1..10).random()) % (vertexCount), (1..10).random())
            graph.addEdge(i, (i + (1..10).random()) % (vertexCount), (1..10).random())
            graph.addEdge(i, (i + (1..10).random()) % (vertexCount), (1..10).random())
            parallelGraph.addEdge(i, i + 1, (1..10).random())
            parallelGraph.addEdge(i, (i + (1..10).random()) % (vertexCount), (1..10).random())
            parallelGraph.addEdge(i, (i + (1..10).random()) % (vertexCount), (1..10).random())
            parallelGraph.addEdge(i, (i + (1..10).random()) % (vertexCount), (1..10).random())
        }

        val sequentialTime = measureTimeMillis {
            dijkstraClassic.dijkstra(0, graph)
        }

        println("Classic Dijkstra time: $sequentialTime ms")

        val parallelTime = measureTimeMillis {
            dijkstraParallel.parallelDijkstra(0, parallelGraph)
        }

        println("Parallel Dijkstra time: $parallelTime ms")

        val parallelThreadsTime = measureTimeMillis {
            dijkstraThreads.parallelDijkstraThreads(0, graph)
        }

        println("Parallel Dijkstra (Threads) time: $parallelThreadsTime ms")
    }
}