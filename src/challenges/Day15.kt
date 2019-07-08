package challenges

import utils.importDataStr
import java.lang.Exception

// Anything that occupies a space on the grid
sealed class Location(val x: Int, val y: Int) : Comparable<Location> {
    
    override operator fun compareTo(other: Location): Int {
        return if (y > other.y || (y == other.y && x > other.x))
            1
        else if (y == other.y && x == other.x)
            0
        else
            -1
    }
}

class Wall(x: Int, y: Int) : Location(x, y)
class EmptySpace(x: Int, y: Int) : Location(x, y)

class Warrior(val health: Int, private val attack: Int, x: Int, y: Int, val type: CharacterType, val movedOn: Int = -1) : Location(x, y) {
    
    enum class CharacterType {
        GOBLIN, ELF
    }
    
    fun attack(defender: Warrior): Location {
        return if (defender.health - attack <= 0)
            EmptySpace(defender.x, defender.y)
        else
            Warrior(defender.health - attack, defender.attack, defender.x, defender.y, defender.type)
    }
    
    fun stepTo(space: EmptySpace, turnCount: Int): Warrior{
        return Warrior(health, attack, space.x, space.y, type, turnCount)
    }
}

typealias GameMap = MutableList<MutableList<Location>>

fun initialGameMap(): GameMap {
    val input = importDataStr(15)
    
    return (0 until input.size).map { y ->
        (0 until input[y].length).map { x ->
            when (input[y][x]) {
                '#' -> Wall(x, y)
                '.' -> EmptySpace(x, y)
                'G' -> Warrior(200, 3, x, y, Warrior.CharacterType.GOBLIN)
                'E' -> Warrior(200, 3, x, y, Warrior.CharacterType.ELF)
                else -> throw Exception("Invalid character ${input[y][x]}")
            }
        }.toMutableList()
    }.toMutableList()
}

// Locations one horizontal or vertical step away - will break for outer walls
fun locationsInRange(location: Location, map: GameMap): List<Location> {
    return listOf(
        map[location.y - 1][location.x],
        map[location.y][location.x - 1],
        map[location.y][location.x + 1],
        map[location.y + 1][location.x]
    )
}

fun selectMeleeTarget(warrior: Warrior, locationsInRange: List<Location>): Warrior? {
    return locationsInRange
        .filterIsInstance<Warrior>()
        .filter{ it.type != warrior.type }
        .groupBy{ it.health }
        .minBy{it.key}?.value?.min()
}

fun targetLocation(warrior: Warrior, map: GameMap): EmptySpace? {
    return map
        .flatMap{ it.filterIsInstance<Warrior>() }
        .filter { it.type != warrior.type }
        .flatMap { locationsInRange(it, map) }
        .filterIsInstance<EmptySpace>()
        .groupBy{findPath(warrior, it, map).second}
        .minBy{it.key}?.value?.min()
}

fun battleWon(warrior: Warrior, map: GameMap): Boolean{
    return map
        .flatMap{ it.filterIsInstance<Warrior>() }
        .none { it.type != warrior.type }
}

// Breadth first search, returning the first step on that path and its length
fun findPath(start: Warrior, end: EmptySpace, map: GameMap): Pair<EmptySpace?, Int> {
    
    var currentPaths = locationsInRange(start, map).filterIsInstance<EmptySpace>().map{ it to setOf(it) }.toMap()
    val explored = currentPaths.map{it.key}.toMutableSet()
    var depth = 0
    
    while(currentPaths.any{ it.value.isNotEmpty() }) {
        depth++
        
        val nextPaths = mutableMapOf<EmptySpace, Set<EmptySpace>>()
        
        currentPaths.forEach { (firstStep, currentLocs) ->
            if (currentLocs.contains(end))
                return@findPath Pair(firstStep, depth)
            
            nextPaths[firstStep] = currentLocs.flatMap{ 
                locationsInRange(it, map)
                    .filterIsInstance<EmptySpace>()
                    .filterNot { explored.contains(it) }
            }.toSet()
            
            explored.addAll(nextPaths[firstStep]!!)
        }

        currentPaths = nextPaths
    }
    return Pair(null, Int.MAX_VALUE)
}
    
data class Outcome(val length: Int, val remainingHealth: Int)

fun battle(): Outcome {
    val map = initialGameMap()
    var turnCount = 0

    while (true) {
        for (y in 0 until map.size){
            for (x in 0 until map[y].size){
                if (map[y][x] !is Warrior)
                    continue
                
                val currentChar = map[y][x] as Warrior
                
                if (currentChar.movedOn == turnCount)
                    continue

                if (battleWon(currentChar, map))
                    return Outcome(turnCount, map.flatten().filterIsInstance<Warrior>().sumBy { it.health })   
                
                val target = selectMeleeTarget(currentChar, locationsInRange(currentChar, map))

                if (target != null) {
                    map[target.y][target.x] = currentChar.attack(target)
                } else {
                    val targetLocation = targetLocation(currentChar, map) ?: continue
                    val nextStep = findPath(currentChar, targetLocation, map).first ?: continue
                    val movedChar = currentChar.stepTo(nextStep, turnCount)
                    map[nextStep.y][nextStep.x] = movedChar
                    map[currentChar.y][currentChar.x] = EmptySpace(currentChar.x, currentChar.y)

                    val newTarget = selectMeleeTarget(movedChar, locationsInRange(movedChar, map))

                    if (newTarget != null)
                        map[newTarget.y][newTarget.x] = movedChar.attack(newTarget)
                }
            }
        }
        turnCount++
    }
        
}

private fun first(): Int {
    val outcome = battle()
    println("${outcome.length} * ${outcome.remainingHealth}")
    return outcome.length * outcome.remainingHealth
}

private fun second() {

}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}