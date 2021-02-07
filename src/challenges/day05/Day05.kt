package challenges.day05

import utils.importDataStr
import java.util.*

fun getConsolidatedSize(polymerString: String): Int {
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

fun filteredPolymer(polymer: String): Int {
    return ('A'..'Z').map { letter ->
        getConsolidatedSize(polymer.filterNot { it == letter || it == letter.toLowerCase() })
    }.minOrNull()!!
}

fun main() {
    val input = importDataStr(5)[0]

    println("First solution: ${getConsolidatedSize(input)}")
    println("Second solution: ${filteredPolymer(input)}")
}