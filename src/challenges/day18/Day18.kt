package challenges.day18

import utils.importDataStr
import utils.repeat

private val input = importDataStr(18)
private val height = input.size
private val width = input[0].length

private const val OPEN = '.'
private const val TREE = '|'
private const val LUMBERYARD = '#'

private fun advanceOneMinute(state: List<String>): List<String>{
    return (state.indices).map { y ->
        (state[y].indices).map { x ->
            val tile = state[y][x]
            val adjacentTiles = (y-1..y+1).flatMap { adjY -> (x - 1..x + 1).map { adjX -> Pair(adjX, adjY) } }
                .filter{ it.first in (0 until width) }
                .filter{ it.second in (0 until height) }
                .filter{ it.first != x || it.second != y }
                .groupingBy{ state[it.second][it.first] }
                .eachCount()
            
            when (tile) {
                OPEN -> if (adjacentTiles.getOrDefault(TREE, 0) >= 3) TREE else OPEN
                TREE -> if (adjacentTiles.getOrDefault(LUMBERYARD, 0) >= 3) LUMBERYARD else TREE
                LUMBERYARD -> if (adjacentTiles.containsKey(LUMBERYARD) && adjacentTiles.containsKey(TREE)) LUMBERYARD else OPEN
                else -> throw Error("$tile is not a valid tile type")
            }
        }.joinToString("")
    }
}

private fun progressProject(initialState: List<String>, minutes: Int): List<String> {
    return repeat(initialState, ::advanceOneMinute, minutes.toLong())
}

/**
 * Since the progress from each state to the next is calculated deterministically, eventually the system will enter
 * a steady cycle. If any state is found twice, skip recalculating all the intereceding cycles!
 * 
 * Returns a single joined list of all tiles, since we would join them immediately on consuming anyway
 */
private fun progressProjectWithCycles(initialState: List<String>, minutes: Int): String {
    val stateHistory = mutableListOf<String>()
    var currentState = initialState
    
    while (!stateHistory.contains(currentState.joinToString(""))) {
        stateHistory.add(currentState.joinToString(""))
        currentState = advanceOneMinute(currentState)
    }
    
    val cycleStart = stateHistory.indexOf(currentState.joinToString(""))
    val cycleLength = stateHistory.size - cycleStart
    val minsAfterCycle = minutes - cycleStart
    
    return stateHistory[cycleStart + minsAfterCycle.rem(cycleLength)]
}

private fun first(): Int {
    val finalState = progressProject(input, 10).joinToString("").groupingBy{it}.eachCount()
    return finalState[TREE]!! * finalState[LUMBERYARD]!!
}

private fun second(): Int {
    val finalState = progressProjectWithCycles(input, 1000000000).groupingBy{it}.eachCount()
    return finalState[TREE]!! * finalState[LUMBERYARD]!!
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}
