package challenges.day13

import utils.importDataStr

private val input = {
    val inputStrs = importDataStr(13)
    val carts = mutableListOf<Cart>()


    val track = (0 until inputStrs.size).map { y ->
        (0 until inputStrs[y].length).map { x ->
            // Replace sections of track that have carts with the correct direction of track
            val continuesLeft = inputStrs.getOrNull(y)?.getOrNull(x - 1) in listOf('-', '\\', '/', '+')
            val continuesUp = inputStrs.getOrNull(y + 1)?.getOrNull(x) in listOf('|', '\\', '/', '+')
            val continuesRight = inputStrs.getOrNull(y)?.getOrNull(x + 1) in listOf('-', '\\', '/', '+')
            val continuesDown = inputStrs.getOrNull(y - 1)?.getOrNull(x) in listOf('|', '\\', '/', '+')

            when (inputStrs[y][x]) {
                '<' -> {
                    carts.add(Cart(Pair(x, y), CartOrientation.LEFT, TurnDirection.LEFT))
                    when {
                        continuesRight && continuesUp && continuesDown -> {
                            '+'
                        }
                        continuesRight -> '-'
                        continuesUp -> '/'
                        continuesDown -> '\\'
                        else -> throw Exception("Invalid track")
                    }
                }
                '^' -> {
                    carts.add(Cart(Pair(x, y), CartOrientation.UP, TurnDirection.LEFT))
                    when {
                        continuesDown && continuesLeft && continuesRight -> '+'
                        continuesDown -> '|'
                        continuesLeft -> '/'
                        continuesRight -> '\\'
                        else -> throw Exception("Invalid track")
                    }
                }
                '>' -> {
                    carts.add(Cart(Pair(x, y), CartOrientation.RIGHT, TurnDirection.LEFT))
                    when {
                        continuesLeft && continuesUp && continuesDown -> '+'
                        continuesLeft -> '-'
                        continuesUp -> '/'
                        continuesLeft -> '\\'
                        else -> throw Exception("Invalid track")
                    }
                }
                'v' -> {
                    carts.add(Cart(Pair(x, y), CartOrientation.DOWN, TurnDirection.LEFT))
                    when {
                        continuesUp && continuesLeft && continuesRight -> '+'
                        continuesUp -> '|'
                        continuesLeft -> '\\'
                        continuesRight -> '/'
                        else -> throw Exception("Invalid track")
                    }
                }
                else -> inputStrs[y][x]
            }
        }
    }

    Pair(track, carts)
}()

val track = input.first
val initialCarts = input.second

/**
 * Tracks the direction a cart is heading in with respect to the overall map
 */
enum class CartOrientation {
    LEFT, UP, RIGHT, DOWN;

    val turnLeft: CartOrientation
        get() = when (this) {
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
            UP -> LEFT
        }

    val turnRight: CartOrientation
        get() = when (this) {
            LEFT -> UP
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
        }
}

/**
 * Tracks the directions a cart will turn relative to its own path
 */
enum class TurnDirection {

    LEFT, AHEAD, RIGHT;

    val nextTurnDirection: TurnDirection
        get() = when (this) {
            LEFT -> AHEAD
            AHEAD -> RIGHT
            RIGHT -> LEFT
        }

    fun nextOrientation(prevOrientation: CartOrientation): CartOrientation =
        when (this) {
            LEFT -> prevOrientation.turnLeft
            AHEAD -> prevOrientation
            RIGHT -> prevOrientation.turnRight
        }

}

class Cart(val position: Pair<Int, Int>, val orientation: CartOrientation, val turnDirection: TurnDirection) {

    fun progressCart(): Cart {
        return Cart(nextPos, nextOrientation, nextTurnDirection)
    }

    private val nextPos: Pair<Int, Int>
        get() =
            when (orientation) {
                CartOrientation.LEFT -> Pair(position.first - 1, position.second)
                CartOrientation.UP -> Pair(position.first, position.second - 1)
                CartOrientation.RIGHT -> Pair(position.first + 1, position.second)
                CartOrientation.DOWN -> Pair(position.first, position.second + 1)
            }

    private val nextTrackPiece get() = track[nextPos.second][nextPos.first]

    private val nextOrientation: CartOrientation
        get() =
            when (nextTrackPiece) {
                '+' -> turnDirection.nextOrientation(orientation)
                '\\' -> when (orientation) {
                    // Top right corner
                    CartOrientation.RIGHT -> CartOrientation.DOWN
                    CartOrientation.UP -> CartOrientation.LEFT

                    // Bottom left corner
                    CartOrientation.LEFT -> CartOrientation.UP
                    CartOrientation.DOWN -> CartOrientation.RIGHT
                }
                '/' -> when (orientation) {
                    // Top left corner
                    CartOrientation.LEFT -> CartOrientation.DOWN
                    CartOrientation.UP -> CartOrientation.RIGHT

                    // Bottom right corner
                    CartOrientation.RIGHT -> CartOrientation.UP
                    CartOrientation.DOWN -> CartOrientation.LEFT
                }
                '|' -> when (orientation) {
                    CartOrientation.UP -> CartOrientation.UP
                    CartOrientation.DOWN -> CartOrientation.DOWN
                    else -> throw Exception("Moving horizontally on vertical track at $nextPos")
                }
                '-' -> when (orientation) {
                    CartOrientation.LEFT -> CartOrientation.LEFT
                    CartOrientation.RIGHT -> CartOrientation.RIGHT
                    else -> throw Exception("Moving vertically on horizontal track at $nextPos")
                }
                else -> throw Exception("Unrecognised track type $nextTrackPiece at $nextPos")
            }

    private val nextTurnDirection: TurnDirection
        get() =
            if (nextTrackPiece == '+') turnDirection.nextTurnDirection else turnDirection
}

fun progressCarts(carts: List<Cart>): List<Cart> {
    return carts.map { it.progressCart() }
}

tailrec fun nextCrashLoc(carts: List<Cart>): Pair<Int, Int> {

    val positions = carts.map { it.position }.toMutableSet()
    carts.sortedWith(compareBy({ it.position.second }, { it.position.first })).map { cart ->
        val newCart = cart.progressCart()
        if (newCart.position in positions)
            return newCart.position
        positions.remove(cart.position)
        positions.add(newCart.position)
        newCart
    }
    return nextCrashLoc(progressCarts(carts))
}

tailrec fun lastCarLoc(carts: List<Cart>): Pair<Int, Int> {

    val positions = carts.map { it.position }.toMutableSet()

    val crashLocsThisTick = mutableListOf<Pair<Int, Int>>()

    println("NEW TICK")
    
    val survivingCarts = carts.sortedWith(compareBy({ it.position.second }, { it.position.first }))
        .mapNotNull { cart ->
            println(cart.position)
            // First check that no cart crashed into this cart
            if (cart.position !in crashLocsThisTick) {
                println("{$cart.position} not in $crashLocsThisTick")
                val newCart = cart.progressCart()
                // Then check that this cart doesn't move into the position of another cart
                if (newCart.position in positions) {
                    println("Car caused crash moving from ${cart.position} to  ${newCart.position}")
                    crashLocsThisTick.add(newCart.position)
                    null
                } else {
                    positions.remove(cart.position)
                    positions.add(newCart.position)
                    newCart
                }
            } else {
                println("Car crashed into at ${cart.position}")
                null
            }
        }.filterNot{it.position in crashLocsThisTick} // Also filter out any carts that already moved crashed into by later carts

    if (survivingCarts.size == 1) {
        return survivingCarts.first().position
    }

    if (survivingCarts.isEmpty()) {
        throw Exception("No carts surviving, valid solution cannot be found")
    }

    println("Remaining size: ${survivingCarts.size}")

    return lastCarLoc(survivingCarts)
}

private fun first(): Pair<Int, Int> {
    return nextCrashLoc(initialCarts)
}

private fun second(): Pair<Int, Int> {
    return lastCarLoc(initialCarts)
}

fun main() {

    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}