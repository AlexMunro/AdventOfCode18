package challenges.day09

import utils.doublyLinkedListOf
import utils.importDataStr

val regexRead = {

    val input = importDataStr(9)[0]
    val regex = Regex("(\\d*) players; last marble is worth (\\d*) points")
    val groups = regex.find(input)!!.groups
    Pair(groups[1]!!.value.toInt(), groups[2]!!.value.toInt())
}()

val playerCount = regexRead.first
val finalMarbleScore = regexRead.second

/**
 * Goes through a game with given number of player and turns, returning the maximum score
 */
fun getMaxScore(players: Int, turns: Int): Long {
    val marbleCircle = doublyLinkedListOf(0) // forward = clockwise, backward = anticlockwise
    var currentPlayer = 0
    val scoreMap = (0 until players).map { it to (0.toLong()) }.toMap().toMutableMap()

    for (currentMarbleScore in (1..turns)) { // Start at 1 because the 0 marble doesn't count

        if (currentMarbleScore % 23 == 0) {
            marbleCircle.moveHead(-7)
            scoreMap[currentPlayer] = scoreMap[currentPlayer]!! + marbleCircle.remove() + currentMarbleScore
        } else {
            marbleCircle.moveHead(1)
            marbleCircle.insertNextAndShift(currentMarbleScore)
        }

        currentPlayer = (currentPlayer + 1) % players
    }

    return scoreMap.values.max()!!
}

private fun first(): Long {
    return getMaxScore(playerCount, finalMarbleScore)
}

private fun second(): Long {
    return getMaxScore(playerCount, finalMarbleScore * 100)
}


fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}