package challenges.day01

import utils.importDataInt

private val input = importDataInt(1)

/**
 * Simple sum of the input. Conversion from string to int handles sign parsing.
 */
private fun first(): Int = input.sum()

/**
 * Starting from zero, incrementally total until the first duplicate total is found. This may require the input to loop!
 */
private fun second(): Int {

    var runningTotal = 0
    val encounteredTotals = mutableSetOf(runningTotal)
    do{
        input.forEach{
            runningTotal += it
            if (runningTotal in encounteredTotals){
                return runningTotal
            } else {
                encounteredTotals.add(runningTotal)
            }
        }
    } while (true) // Will continually execute if no duplicate sum is found!
}


fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}