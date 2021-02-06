package challepnges.day17

import utils.importDataStr
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Water falling!
 *
 * Since this one is tricky to reason about but easy to draw, so running this solution will also output a visualisation.
 */

private val input = importDataStr(17)

private val clayPositions: Set<Pair<Int, Int>> =
    input.flatMap {
        val bounds = Regex("""^.=(\d*), .=(\d*)..(\d*)$""").find(it)!!.groupValues

        if (it[0] == 'x') {
            (bounds[2].toInt()..bounds[3].toInt()).map {
                Pair(bounds[1].toInt(), it)
            }
        } else {
            (bounds[2].toInt()..bounds[3].toInt()).map {
                Pair(it, bounds[1].toInt())
            }
        }
    }.toSet()

val minX = clayPositions.minByOrNull { it.first }!!.first - 1
val maxX = clayPositions.maxByOrNull { it.first }!!.first + 1
val minY = clayPositions.minByOrNull { it.second }!!.second
val maxY = clayPositions.maxByOrNull { it.second }!!.second

fun visualise(
    clayPositions: Set<Pair<Int, Int>>,
    waterPositions: Set<Pair<Int, Int>>,
    settledWater: Set<Pair<Int, Int>>,
    filename: String
) {
    val CLAY = Color.decode("#A54A40")
    val WATER = Color.CYAN
    val BACKGROUND = Color.WHITE
    val DRAINS = Color.BLUE

    val width = maxX - minX + 1
    val depth = maxY + 1

    val xOffset = 0 - minX

    val bi = BufferedImage(width, depth, BufferedImage.TYPE_INT_RGB)
    val g2d = bi.createGraphics()
    g2d.paint = BACKGROUND
    g2d.fillRect(0, 0, width, depth)

    clayPositions.forEach { bi.setRGB(it.first + xOffset, it.second, CLAY.rgb) }
    waterPositions.forEach { bi.setRGB(it.first + xOffset, it.second, WATER.rgb) }
    settledWater.forEach { bi.setRGB(it.first + xOffset, it.second, DRAINS.rgb) }

    val outfile = File(filename)
    try {
        ImageIO.write(bi, "bmp", outfile)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun waterPositions(): Pair<Set<Pair<Int, Int>>, Set<Pair<Int, Int>>> {

    val allWaterPositions = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
    val settledWater = mutableSetOf<Pair<Int, Int>>()

    fun spaceOccupied(x: Int, y: Int) = clayPositions.contains(Pair(x, y)) || allWaterPositions.contains(Pair(x, y))
    fun settledSpace(x: Int, y: Int) = clayPositions.contains(Pair(x, y)) || settledWater.contains(Pair(x, y))

    // Checks from the left if
    tailrec fun waterCanSettle(x: Int, y: Int, direction: Int): Boolean {
        if (clayPositions.contains(Pair(x, y))) return true
        if (allWaterPositions.contains(Pair(x, y)) || settledSpace(x, y + 1)) return waterCanSettle(
            x + direction,
            y,
            direction
        )
        return false
    }

    // Only invoke if waterCanSettle is true!
    tailrec fun markAsSettled(x: Int, y: Int, direction: Int) {
        val pos = Pair(x, y)
        if (!clayPositions.contains(pos)) {
            // No point in storing the parent of settled water
            if (!allWaterPositions.contains(pos)) allWaterPositions[pos] = null
            settledWater.add(pos)
            markAsSettled(x + direction, y, direction)
        }
    }

    tailrec fun flowWater(x: Int, y: Int, parent: Pair<Int, Int>?) {
        val currentPos = Pair(x, y)

        val newlyOccupiedSpace = !allWaterPositions.contains(currentPos)
        if (newlyOccupiedSpace) allWaterPositions[currentPos] = parent

        // Stops tracking water that flows beyond maximum depth
        if (y == maxY) return

        // Continue down if not currently bubbling back up or hitting clay
        if (!spaceOccupied(x, y + 1)) return flowWater(x, y + 1, currentPos)

        val canGoLeft = !spaceOccupied(x - 1, y)
        val canGoRight = !settledWater.contains(currentPos) && !spaceOccupied(x + 1, y)

        if (clayPositions.contains(Pair(x - 1, y)) && waterCanSettle(x, y, 1)) {
            markAsSettled(x, y, 1)
        }

        if (clayPositions.contains(Pair(x + 1, y)) && waterCanSettle(x, y, -1)) {
            markAsSettled(x, y, -1)
        }

        if (settledSpace(x, y + 1)) { // Only spread over clay or settled water
            if (canGoLeft && canGoRight) {
                flowWater(x - 1, y, currentPos)
                if (!settledWater.contains(currentPos)) return flowWater(x + 1, y, currentPos)
            }

            // Flow to the left and/or the right if possible
            if (canGoLeft) return flowWater(x - 1, y, currentPos)
            if (canGoRight) return flowWater(x + 1, y, currentPos)
        }

        // Bubble back up 
        if (parent != null) return flowWater(parent.first, parent.second, allWaterPositions[parent])
    }
    flowWater(500, 0, null)

    visualise(clayPositions, allWaterPositions.keys, settledWater, "output/17.bmp")
    return Pair(allWaterPositions.keys, settledWater)
}

private fun first(): Int {
    return waterPositions().first.filter { it.second in minY..maxY }.size
}

private fun second(): Int {
    return waterPositions().second.size
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}
