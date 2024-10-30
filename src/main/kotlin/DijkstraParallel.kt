import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.PriorityBlockingQueue

class DijkstraParallel {
    suspend fun parallelDijkstra(start: Int, graph: ParallelGraph): IntArray {
        val distances = IntArray(graph.vertices) { Int.MAX_VALUE }
        distances[start] = 0

        val priorityQueue = PriorityBlockingQueue<Pair<Int, Int>>(10,compareBy { it.second })
        priorityQueue.add(Pair(start, 0))
        val visited = ConcurrentHashMap<Int, Boolean>()

        coroutineScope {
            while (priorityQueue.isNotEmpty()) {
                val tasks = mutableListOf<Deferred<Unit>>()
                repeat(priorityQueue.size) {
                    val (currentVertex, currentDistance) = priorityQueue.poll() ?: return@coroutineScope

                    if (visited.putIfAbsent(currentVertex, true) != null) return@coroutineScope

                    tasks.add(async {
                        for (edge in graph.adjList[currentVertex]) {
                            val newDistance = currentDistance + edge.weight
                            graph.mutex.withLock {
                                if (newDistance < distances[edge.destination]) {
                                    distances[edge.destination] = newDistance
                                    priorityQueue.add(Pair(edge.destination, newDistance))
                                }
                            }
                        }
                    })
                }
                tasks.awaitAll()
            }
        }
        return distances
    }
}