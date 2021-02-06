package challenges.day12

import utils.importDataStr
import utils.longVariance
import utils.repeat
import kotlin.system.measureTimeMillis

private val input = importDataStr(12)

/**
 * This challenge implements a 1D cellular automaton with two neighbours on each side.
 * Both parts sum the indices of live cells.
 */

/**
 * Store
 */
private val initialState: Set<Long> = {
    val stringRep = input[0].drop("initial state: ".length)
    (0 until stringRep.length).filter { stringRep[it] == '#' }.map { it.toLong() }.toSet()
}()

val neighbourhoodsWithFlowers: Set<Int> = input.drop(2).filter { it.last() == '#' }
    .map { it.take(5).fold(0) { binary, hasFlower -> (binary shl 1) + (if (hasFlower == '#') 1 else 0) } }.toSet()


val nextState: (Set<Long>) -> Set<Long> = { state ->

    val newMin = state.min()!! - 2
    val newMax = state.max()!! + 2

    (newMin..newMax).mapNotNull { idx ->
        val neighborhood = (idx - 2..idx + 2).map {
            state.contains(it) // Assume any untracked pots have no flowers
        }.fold(0) { binary, boolean -> (binary shl 1) + (if (boolean) 1 else 0) }

        if (neighbourhoodsWithFlowers.contains(neighborhood)) idx else null

    }.toSet()
}

/**
 * Sum of the indexes of all pots containing plants n generations after state
 */
fun sumPlantPotsAfter(state: Set<Long>, n: Long): Long {

     return repeat(state, nextState, n).sum()
}


const val CONVERGENCE_THRESHOLD = 2

/**
 * Sums the indexes of pots after n generations for very large n
 */
fun sumPlantPotsShortcut(state: Set<Long>, n: Long) : Long {

    var spans = List<Long>(CONVERGENCE_THRESHOLD) {-1}
    var variances = List(CONVERGENCE_THRESHOLD) {-1.0}
    var currentState = state
    var iters = 0
    var prevMin: Long
    // Find a point of convergence
    do {
        iters += 1
        prevMin = currentState.min()!!
        currentState = nextState(currentState)
        spans = spans.drop(1).plus(currentState.max()!! - currentState.min()!!)
        variances = variances.drop(1).plus(longVariance(currentState))
    } while (spans.distinct().size > 1 || variances.distinct().size > 1)

    println("$iters iters")
    val diff = currentState.min()!! - prevMin // Determines the stable diff in each cell (in {-1, 0, +1})

    return currentState.sum() + (currentState.size * diff * (n - iters))

}


private fun first(): Long {
    return sumPlantPotsAfter(initialState, 20)
}

private fun second(): Long {
    return sumPlantPotsShortcut(initialState, 50_000_000_000L)
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
    println(measureTimeMillis{ second() })
}