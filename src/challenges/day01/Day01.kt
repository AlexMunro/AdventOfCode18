package challenges.day01

import utils.importDataInt

/**
 * Simple sum of the input. Conversion from string to int handles sign parsing.
 */
fun sum(xs: List<Int>): Int = xs.sum()

/**
 * Starting from zero, incrementally total until the first duplicate total is found. This may require the input to loop!
 */
fun firstDuplicateSum(xs: List<Int>): Int {
    var runningTotal = 0
    val encounteredTotals = mutableSetOf(runningTotal)
    do {
        xs.forEach {
            runningTotal += it
            if (runningTotal in encounteredTotals) {
                return runningTotal
            } else {
                encounteredTotals.add(runningTotal)
            }
        }
    } while (true)
}


fun main() {
    val input = importDataInt(1)

    println("First solution: ${sum(input)}")
    println("Second solution: ${firstDuplicateSum(input)}")
}