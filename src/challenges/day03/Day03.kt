package challenges.day03

import utils.importDataStr

data class Rect(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int)

/**
 *   Parses input strings (e.g. "#1 @ 704,926: 5x4") into a list of Rect
 */
fun parseRects(claims: List<String>): List<Rect> {
    val regex = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")

    return claims.map {
        val matchedGroups = regex.find(it)!!.groups
        // Safe to assume all parsed lines contain valid rectangle descriptions
        Rect(
            matchedGroups[1]!!.value.toInt(), matchedGroups[2]!!.value.toInt(),
            matchedGroups[3]!!.value.toInt(), matchedGroups[4]!!.value.toInt(), matchedGroups[5]!!.value.toInt()
        )
    }
}

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
fun pointsMap(claims: List<String>): HashMap<Pair<Int, Int>, Int> {
    val map = HashMap<Pair<Int, Int>, Int>()
    parseRects(claims).forEach { rect ->
        rectPoints(rect).forEach { point ->
            map[point] = (map[point] ?: 0) + 1
        }
    }
    return map
}

/**
 * Given a list of rectangles, number of points (with integer co-ords) within more than one rectangle
 */
fun overlappedPoints(claims: List<String>): Int {
    return pointsMap(claims).values.filter { it > 1 }.size
}

/**
 * Return the ID of the rectangle which does not overlap with any other rectangle
 */
fun nonOverlappingRectangle(claims: List<String>): Int {
    val points = pointsMap(claims)

    // Check the points map to ensure that every contained point is occupied by exactly one rectangle
    parseRects(claims).forEach { rect ->
        if (rectPoints(rect).all { (points[it] == 1) }) {
            return rect.id
        }
    }
    throw Exception("There is no such rectangle that does not overlap with any other rectangle.")
}

fun main() {
    val input = importDataStr(3)

    println("First solution: ${overlappedPoints(input)}")
    println("Second solution: ${nonOverlappingRectangle(input)}")
}