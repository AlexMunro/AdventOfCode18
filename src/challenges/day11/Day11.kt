package challenges.day11

import utils.importDataSingleInt

private val gridSerialNumber = importDataSingleInt(11)

const val SIZE = 300

fun cellValue(x: Int, y: Int): Int {
    val rackId = x + 10
    var powerLevel = rackId * y
    powerLevel += gridSerialNumber
    powerLevel *= rackId
    powerLevel /= 100
    powerLevel %= 10
    powerLevel -= 5
    return powerLevel
}

/**
 * List of points with cartesian product of points (minX..maxX, minY..maxY)
 */
fun grid(minX: Int, maxX: Int, minY: Int, maxY: Int): List<Pair<Int, Int>> =
    (minX..maxX).flatMap { x -> (minY..maxY).map { y -> Pair(x, y) } }

val cellPowerMap: Map<Pair<Int, Int>, Int> = grid(1, 300, 1, 300)
    .map { (x, y) -> Pair(x, y) to cellValue(x, y) }.toMap()

val gridPowerMap: MutableMap<Triple<Int, Int, Int>, Int> = mutableMapOf()

/**
 * Sums the power values of a size x size grid, with top-left point (x,y)
 */
fun gridPower(x: Int, y: Int, size: Int): Int {
    // println("Checking $x $y at size $size")
    if (size == 1) {
        return cellPowerMap[Pair(x, y)]!!
    }
    if (gridPowerMap[Triple(x, y, size)] != null) {
        return gridPowerMap[Triple(x, y, size)]!!
    }

    val power = gridPower(x, y, size - 1) +
            (x until (x + size - 1)).map { Pair(it, (y + size - 1)) }
                .union((y until (y + size - 1)).map { Pair((x + size - 1), it) }).plus(Pair(x + size - 1, y + size - 1))
                .map { (x, y) -> cellPowerMap[Pair(x, y)]!! }.sum().also { gridPowerMap[Triple(x, y, size)] = it }


    gridPowerMap[Triple(x, y, size)] = power

    return power
}

/**
 * (x,y) position of 3x3 grid with most power
 */
private fun first(): Pair<Int, Int> {
    return grid(1, SIZE - 3, 1, SIZE - 3)
        .maxBy { (x, y) -> gridPower(x, y, 3) }!!
}

/**
 * (x,y) position and corresponding size of square grid with most power of any size
 */
private fun second(): Triple<Int, Int, Int> {

    println(gridPower(233, 187, 13))

    return grid(1, SIZE, 1, SIZE)
        .flatMap { (x, y) -> (1..SIZE).map { size -> Triple(x, y, size) } }
        .filter { (x, y, size) -> (x + size - 1 <= SIZE) && (y + size - 1 <= SIZE) }
        .groupBy { (x, y, size) -> gridPower(x, y, size) }
        .maxBy { it.key }!!.value.first()

}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}