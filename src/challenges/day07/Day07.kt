package challenges.day07

import utils.importDataStr

/**
 * Dictionary of graph nodes to a list of pre-requisites
 */
val dependencyMap : Map<Char, List<Char>> = {

    val input = importDataStr(7)
    val map = ('A'..'Z').map{it to listOf<Char>() }.toMap().toMutableMap() // Instantiating first to preserve alphabetical order
    for (line in input){
        // Input of the form e.g. "Step M must be finished before step S can begin.
        // Hard coding the positions of these characters instead of using a regex

        val node = line[36]
        val preReq = line[5]

        // Using immutable list so that shallow clones do not share lists
        map[node] = map[node]!!.plus(preReq)
    }
    map
}()

/**
 * Given a dependency graph of nodes, return the correct order of nodes using alphabetical order as a tie-breaker
 */
private fun first() : String {

    val map = dependencyMap.toMutableMap() // Mutable clone of original map
    var result = ""

    while (!map.isEmpty()){
        // Iterating in alphabetical order
        for ((char, preReqs) in map){
            // Add char to the result
            if (preReqs.filterNot{result.contains(it)}.isEmpty()){
                map.remove(char)
                result += char
                break // Return to start of for loop to ensure alphabetical order of result
            }
        }
    }

    return result
}

/**
 * Determine how long it would take a number of workers to concurrently process each node, given a time cost for each
 */
private fun second() : Int {

    val workerCount = 5

    var finished = ""
    var inProgress = mutableMapOf<Char, Int>() // Map of chars to seconds of work left
    var remaining = ('A'..'Z').toList()
    var secondsElapsed = 0

    fun getNext() : Char? = remaining.firstOrNull{dependencyMap[it]!!.all{finished.contains(it)}}

    while(!(remaining.isEmpty() && inProgress.isEmpty())){

        // Assign as many workers while there are workers still available and work that can be immediately started
        while ( inProgress.size < workerCount  && getNext() != null) {
            val node = getNext()!!
            inProgress.put(node, node.toInt() - 64 + 60 ) // Adding position in alphabet derived from ASCII value and 60 seconds
            remaining = remaining.filterNot { it == node }
        }

        val timeIncrement = inProgress.values.min()!!

        // Decrement work remaining by 1
        for (node in inProgress.keys){
            inProgress[node] = inProgress[node]!! - timeIncrement
            if (inProgress[node] == 0){
                finished += node
            }
        }

        // Filter out any completed characters and advance seconds elapsed
        inProgress = inProgress.filterNot{it.value == 0}.toMutableMap()
        secondsElapsed += timeIncrement

    }

    return secondsElapsed

}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}