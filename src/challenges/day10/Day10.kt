package challenges.day10

import utils.importDataSingleStr

// Represent stars as sequence of pairs of (x,y) position and (x,y) velocity
typealias StarData = List<Pair<Pair<Int, Int>, Pair<Int, Int>>>

val starData: StarData = Regex("position=< *(-?\\d+), *(-?\\d+)> velocity=< *(-?\\d+), *(-?\\d+)>")
    .findAll(importDataSingleStr(10))
    .map {
        Pair(
            Pair(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt()),
            Pair(it.groups[3]!!.value.toInt(), it.groups[4]!!.value.toInt())
        )
    }.toList()

fun advanceStars(stars: StarData): StarData {
    return stars.map {
        // Advance position, pass along velocity
        Pair(Pair(it.first.first + it.second.first, it.first.second + it.second.second), it.second)
    }
}

fun orderX(stars: StarData): StarData = stars.sortedBy { it.first.first }
fun orderY(stars: StarData): StarData = stars.sortedBy { it.first.second }


/**
 * Returns the positions of stars at the second of maximum convergence (minimum span of X and Y axes)
 */
tailrec fun convergencePoint(
    stars: StarData,
    prevStars: StarData,
    prevSpans: Int,
    secondsPassed: Int
): Pair<StarData, Int> {
    // val sumDeviation = stdDev(stars.map { it.first.first }) + stdDev(stars.map { it.first.second })
    val xOrder = orderX(stars)
    val yOrder = orderY(stars)
    val sumSpans =
        (xOrder.last().first.first - xOrder.first().first.first) + (yOrder.last().first.second - yOrder.last().first.second)

    return if (prevSpans < sumSpans) Pair(prevStars, secondsPassed - 1)
    else convergencePoint(advanceStars(stars), stars, sumSpans, secondsPassed + 1)
}

fun printStars(stars: StarData) {
    val xOrder = orderX(stars.distinctBy { it.first }) // Distinct position makes it easier to print from this list
    val yOrder =
        orderY(xOrder) // Taking advantage of stable sorts in Kotlin (undocumented behaviour, this may age poorly)
    val minX = xOrder.first().first.first
    val maxX = xOrder.last().first.first
    val minY = yOrder.first().first.second
    val maxY = yOrder.last().first.second

    var nextToPrint = 0

    for (y in minY..maxY) {
        var line = ""
        for (x in minX..maxX) {
            if (yOrder.getOrNull(nextToPrint)?.first == Pair(x, y)) {
                line += '#'
                nextToPrint++
            } else {
                line += '.'
            }
        }
        println(line)
    }

}


private fun first() {
    printStars(convergencePoint(starData, starData, Int.MAX_VALUE, 0).first)
}

private fun second(): Int {
    return convergencePoint(starData, starData, Int.MAX_VALUE, 0).second
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}