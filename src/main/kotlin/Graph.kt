class Graph (val vertices: Int) {
    val adjList: Array<MutableList< Edge>> = Array(vertices)
    { mutableListOf() }

    fun addEdge(src: Int, dest: Int, weight: Int) {
        adjList[src].add(Edge(dest, weight))
        adjList[dest].add(Edge(src, weight)) // Для неориентированного графа
    }
}


data class Edge(val destination: Int, val weight: Int)