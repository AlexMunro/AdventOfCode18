package challenges.day13

import utils.importDataStr

typealias Track = List<List<Char>>

fun processInput(representation: List<String>):  Pair<Track, MutableList<Cart>> {
    val carts = mutableListOf<Cart>()

    val track = representation.indices.map { y ->
        representation[y].indices.map { x ->
            // Replace sections of track that have carts with the correct direction of track
            val continuesLeft = representation.getOrNull(y)?.getOrNull(x - 1) in listOf('-', '\\', '/', '+')
            val continuesUp = representation.getOrNull(y + 1)?.getOrNull(x) in listOf('|', '\\', '/', '+')
            val continuesRight = representation.getOrNull(y)?.getOrNull(x + 1) in listOf('-', '\\', '/', '+')
            val continuesDown = representation.getOrNull(y - 1)?.getOrNull(x) in listOf('|', '\\', '/', '+')

            when (representation[y][x]) {
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
                else -> representation[y][x]
            }
        }
    }

    return Pair(track, carts)
}

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

class Cart(val position: Pair<Int, Int>, private val orientation: CartOrientation, private val turnDirection: TurnDirection) {
    fun progressCart(track: Track): Cart {
        return Cart(nextPos, nextOrientation(track), nextTurnDirection(track))
    }

    private val nextPos: Pair<Int, Int>
        get() =
            when (orientation) {
                CartOrientation.LEFT -> Pair(position.first - 1, position.second)
                CartOrientation.UP -> Pair(position.first, position.second - 1)
                CartOrientation.RIGHT -> Pair(position.first + 1, position.second)
                CartOrientation.DOWN -> Pair(position.first, position.second + 1)
            }

    private fun nextTrackPiece(track: Track): Char {
        return track[nextPos.second][nextPos.first]
    }

    private fun nextOrientation(track: Track): CartOrientation {
        return when (nextTrackPiece(track)) {
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
            else -> throw Exception("Unrecognised track type ${nextTrackPiece(track)} at $nextPos")
        }
    }

    private fun nextTurnDirection(track: Track): TurnDirection {
        return if (nextTrackPiece(track) == '+') turnDirection.nextTurnDirection else turnDirection
    }
}

tailrec fun nextCrashLoc(track: Track, carts: List<Cart>): Pair<Int, Int> {
    val positions = carts.map { it.position }.toMutableSet()

    carts.sortedWith(compareBy({ it.position.second }, { it.position.first })).map { cart ->
        val newCart = cart.progressCart(track)
        if (newCart.position in positions)
            return newCart.position
        positions.remove(cart.position)
        positions.add(newCart.position)
        newCart
    }
    return nextCrashLoc(track, carts.map { it.progressCart(track) })
}

tailrec fun lastCarLoc(track: Track, carts: List<Cart>): Pair<Int, Int> {
    val positions = carts.map { it.position }.toMutableSet()

    val crashLocsThisTick = mutableListOf<Pair<Int, Int>>()

    val survivingCarts = carts.sortedWith(compareBy({ it.position.second }, { it.position.first }))
        .mapNotNull { cart ->
            // First check that no cart crashed into this cart
            if (cart.position !in crashLocsThisTick) {
                val newCart = cart.progressCart(track)
                // Then check that this cart doesn't move into the position of another cart
                if (newCart.position in positions) {
                    crashLocsThisTick.add(newCart.position)
                    null
                } else {
                    positions.remove(cart.position)
                    positions.add(newCart.position)
                    newCart
                }
            } else {
                null
            }
        }
        .filterNot { it.position in crashLocsThisTick } // Also filter out any carts that already moved crashed into by later carts

    if (survivingCarts.size == 1) {
        return survivingCarts.first().position
    }

    if (survivingCarts.isEmpty()) {
        throw Exception("No carts surviving, valid solution cannot be found")
    }

    return lastCarLoc(track, survivingCarts)
}

fun main() {
    val (track, initialCarts) = processInput(importDataStr(13))

    println("First solution: ${nextCrashLoc(track, initialCarts)}")
    println("Second solution: ${lastCarLoc(track, initialCarts)}")
}