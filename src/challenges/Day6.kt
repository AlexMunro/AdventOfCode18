package challenges

import utils.importDataStr

private val input = importDataStr(6)

private val coordList: List<Pair<Int, Int>> = {
    val regex = Regex("^(\\d*), (\\d*)\$")
    input.map {
        val matchedGroups = regex.find(it)!!.groups
        Pair(matchedGroups[1]!!.value.toInt(), matchedGroups[2]!!.value.toInt())
    }
}()

// Define the grid we consider for this problem
private val maxX: Int = coordList.map { it.first }.max()!! + 1000
private val maxY: Int = coordList.map { it.second }.max()!! + 1000

fun manhattanDist(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int =
    Math.abs(p1.first - p2.first) + Math.abs(p1.second - p2.second)

/**
 * Returns the index of the closest co-ordinate only if there is no tie between co-ordinates
 */
fun closestCoord(p: Pair<Int, Int>): Int? {
    val closestPoints =
        (0 until coordList.size).map { Pair(it, manhattanDist(p, coordList[it])) }.sortedBy { it.second }
    return if (closestPoints[0].second != closestPoints[1].second) closestPoints[0].first else null
}

fun closerToAllThan(p: Pair<Int, Int>, threshold: Int): Boolean {
    return coordList.sumBy { manhattanDist(it, p) } < threshold
}

private fun first(): Int {

    // Begin by checking for infinite values that have closest values at the outside
    val infiniteIndexes = HashSet<Int>()

    // min/max x
    infiniteIndexes.addAll(arrayOf(0, maxX).flatMap { x ->
        (0..maxY).mapNotNull { y ->
            closestCoord(Pair(x, y))
        }
    })

    // min/max y
    infiniteIndexes.addAll((0..maxX).flatMap { x ->
        arrayOf(0, maxY).mapNotNull { y ->
            closestCoord(Pair(x, y))
        }
    })

    // Calculate the nearest co-ordinate for all points within (1,1) -> (maxX-1, maxY-1), returning the most common index

    return (1 until maxX).flatMap { x ->
        (1 until maxY).mapNotNull { y ->
            closestCoord(Pair(x, y))
        }.filterNot { it in infiniteIndexes }
    }.groupingBy { it }.eachCount().maxBy { it.value }!!.value
}

/**
 * Number of points that have a summed distance from all co-ordinates of less than a threshold
 */
private fun second(): Int {

    // Start at negative threshold to ensure such values are included even thought all co-ords are positive

    val threshold = 10000

    return (-threshold..maxX).flatMap { x ->
        (-threshold..maxY).map { y ->
            closerToAllThan(Pair(x, y), threshold)
        }.filter { it }
    }.size

}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}