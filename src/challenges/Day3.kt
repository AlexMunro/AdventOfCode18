package challenges

import utils.importDataStr

data class Rect(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int)

/**
 *   Parses input strings (e.g. "#1 @ 704,926: 5x4") into a list of Rect
 */
val rects: List<Rect> = {

    val input = importDataStr(3)
    val regex = Regex("#(\\d+) \\@ (\\d+),(\\d+): (\\d+)x(\\d+)")

    input.map {
        val matchedGroups = regex.find(it)!!.groups
        // Safe to assume all parsed lines contain valid rectangle descriptions
        Rect(
            matchedGroups[1]!!.value.toInt(), matchedGroups[2]!!.value.toInt(),
            matchedGroups[3]!!.value.toInt(), matchedGroups[4]!!.value.toInt(), matchedGroups[5]!!.value.toInt()
        )
    }
}()


/**
 * Gets all points a Rect spans
 */
fun rectPoints(rect: Rect): List<Pair<Int, Int>> {
    return (0 until rect.width).flatMap { width ->
        (0 until rect.height).map { height ->
            Pair(rect.x + width, rect.y + height)
        }
    }
}

/**
 * Map of all points spanned by rectangles to the number of rectangles spanning that point
 */
val pointsMap: HashMap<Pair<Int, Int>, Int> = {
    val map = HashMap<Pair<Int, Int>, Int>()
    rects.forEach { rect ->
        rectPoints(rect).forEach { point ->
            map[point] = (map[point] ?: 0) + 1
        }
    }
    map
}()

/**
 * Given a list of rectangles, number of points (with integer co-ords) within more than one rectangle
 */
private fun first(): Int {

    // Naive implementation - find every point and count them in a dictionary
    return pointsMap.values.filter { it > 1 }.size
}

/**
 * Return the ID of the rectangle which does not overlap with any other rectangle
 */
private fun second(): Int {

    // Check the points map to ensure that every contained point is occupied by exactly one rectangle
    rects.forEach{rect ->
        if (rectPoints(rect).all{(pointsMap[it] == 1)}){
            return rect.id
        }
    }
    throw Exception("There is no such rectangle that does not overlap with any other rectangle.")
}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}