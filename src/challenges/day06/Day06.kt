package challenges.day06

import utils.importDataStr
import kotlin.math.abs

private fun parseCoords(coords: List<String>): List<Pair<Int, Int>> {
    val regex = Regex("""^(\d*), (\d*)$""")
    return coords.map {
        val matchedGroups = regex.find(it)!!.groups
        Pair(matchedGroups[1]!!.value.toInt(), matchedGroups[2]!!.value.toInt())
    }
}

// Define the grid we consider for this problem
fun maxX(coords: List<Pair<Int, Int>>): Int = coords.maxByOrNull { it.first }!!.first + 1000
fun maxY(coords: List<Pair<Int, Int>>): Int = coords.maxByOrNull { it.second }!!.second + 1000

fun manhattanDist(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int =
    abs(p1.first - p2.first) + abs(p1.second - p2.second)

/**
 * Returns the index of the closest co-ordinate only if there is no tie between co-ordinates
 */
fun closestCoord(p: Pair<Int, Int>, coords: List<Pair<Int, Int>>): Int? {
    val closestPoints = coords.indices.map { Pair(it, manhattanDist(p, coords[it])) }.sortedBy { it.second }
    return if (closestPoints[0].second != closestPoints[1].second) closestPoints[0].first else null
}

fun closerToAllThan(p: Pair<Int, Int>, threshold: Int, coords: List<Pair<Int, Int>>) =
    coords.sumBy { manhattanDist(it, p) } < threshold

fun largestArea(coordStrings: List<String>): Int {
    val coords = parseCoords(coordStrings)
    val maxX = maxX(coords)
    val maxY = maxY(coords)

    // Check for infinite values that have closest values at the outside
    val infiniteIndexes = HashSet<Int>()

    // min/max x
    infiniteIndexes.addAll(arrayOf(0, maxX(coords)).flatMap { x ->
        (0..maxY).mapNotNull { y -> closestCoord(Pair(x, y), coords) }
    })

    // min/max y
    infiniteIndexes.addAll((0..maxX).flatMap { x ->
        arrayOf(0, maxY).mapNotNull { y -> closestCoord(Pair(x, y), coords) }
    })

    // Calculate the nearest co-ordinate for all points within (1,1) -> (maxX-1, maxY-1), returning the most common index

    return (1 until maxX).flatMap { x ->
        (1 until maxY).mapNotNull { y ->
            closestCoord(Pair(x, y), coords)
        }.filterNot { it in infiniteIndexes }
    }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.value
}

/**
 * Number of points that have a summed distance from all co-ordinates of less than a threshold
 */
fun closePoints(coordStrings: List<String>, threshold: Int): Int {
    val coords = parseCoords(coordStrings)
    val maxX = maxX(coords)
    val maxY = maxY(coords)

    return (-threshold..maxX).flatMap { x ->
        (-threshold..maxY).map { y ->
            closerToAllThan(Pair(x, y), threshold, coords)
        }.filter { it }
    }.size
}

fun main() {
    val input = importDataStr(6)

    println("First solution: ${largestArea(input)}")
    println("Second solution: ${closePoints(input, 10000)}")
}