package challenges.day05

import utils.importDataStr
import java.util.*

private val input = importDataStr(5)[0]


/**
 * Given a string of characters, count the characters left after repeatedly annihilating pairs of the same letter with
 * opposite case
 */
private fun getConsolidatedSize(polymerString: String): Int {
    val charStack = Stack<Char>()
    for (char in polymerString) {
        if (!charStack.isEmpty() && char != charStack.peek() &&
            ((char.isUpperCase() && charStack.peek().toUpperCase() == char) ||
                    (char.isLowerCase() && charStack.peek().toLowerCase() == char))
        ) {
            charStack.pop()
        } else {
            charStack.push(char)
        }
    }
    return charStack.size
}

/**
 * Consolidates the input string and returns its size
 */
private fun first(): Int {
    return getConsolidatedSize(input)
}

/**
 * Repeats the first task after filtering out each letter of the alphabet (upper and lower) to see which results
 * in the smallest consolidated string
 */
private fun second(): Int {
    return ('A'..'Z').map { letter ->
        getConsolidatedSize(input.filterNot { it == letter || it == letter.toLowerCase() })
    }.minOrNull()!!
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}