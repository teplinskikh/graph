import java.util.*

class DijkstraClassic {
    fun dijkstra(start: Int, graph: Graph): IntArray {
        val distances = IntArray(graph.vertices) { Int.MAX_VALUE }
        distances[start] = 0

        val priorityQueue = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
        priorityQueue.add(Pair(start, 0))

        while (priorityQueue.isNotEmpty()) {
            val (currentVertex, currentDistance) = priorityQueue.poll()

            if (currentDistance > distances[currentVertex]) continue

            for (edge in graph.adjList[currentVertex]) {
                val newDistance = currentDistance + edge.weight
                if (newDistance < distances[edge.destination]) {
                    distances[edge.destination] = newDistance
                    priorityQueue.add(Pair(edge.destination, newDistance))
                }
            }
        }
        return distances
    }
}
