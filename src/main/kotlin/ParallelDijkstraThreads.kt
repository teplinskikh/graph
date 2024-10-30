import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import java.util.concurrent.PriorityBlockingQueue

class ParallelDijkstraThreads {
    fun parallelDijkstraThreads(start: Int, graph: Graph): IntArray {
        val distances = IntArray(graph.vertices) { Int.MAX_VALUE }
        distances[start] = 0

        val priorityQueue = PriorityBlockingQueue<Pair<Int, Int>>(10, compareBy { it.second })
        priorityQueue.add(Pair(start, 0))
        val visited = ConcurrentHashMap.newKeySet<Int>()

        val numThreads = Runtime.getRuntime().availableProcessors()
        val threads = mutableListOf<Thread>()

        repeat(numThreads) {
            val thread = thread {
                while (priorityQueue.isNotEmpty()) {
                    val (currentVertex, currentDistance) = priorityQueue.poll() ?: continue

                    if (!visited.add(currentVertex)) continue
                    for (edge in graph.adjList[currentVertex]) {
                        val newDistance = currentDistance + edge.weight
                        if (newDistance < distances[edge.destination]) {
                            distances[edge.destination] = newDistance
                            priorityQueue.add(Pair(edge.destination, newDistance))
                        }
                    }
                }
            }
            threads.add(thread)
        }

        threads.forEach { it.join() }

        return distances
    }
}
