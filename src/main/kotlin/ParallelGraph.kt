import kotlinx.coroutines.sync.Mutex

class ParallelGraph(val vertices: Int) {
    val adjList: Array<MutableList<Edge>> = Array(vertices)
    { mutableListOf() }
    val mutex = Mutex()

    fun addEdge(src: Int, dest: Int, weight: Int) {
        adjList[src].add(Edge(dest, weight))
        adjList[dest].add(Edge(src, weight))
    }
}