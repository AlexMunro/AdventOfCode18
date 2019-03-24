package challenges

import utils.importDataSingleInt
import java.lang.Character.getNumericValue

private val input = importDataSingleInt(14)

val initialRecipes = listOf(3, 7)

fun newRecipes(recipes: Pair<Int, Int>) : List<Int> =
    (recipes.first + recipes.second).toString().map { getNumericValue(it) }

fun firstNRecipes(n: Int) : List<Int> {
    var firstElf = 0
    var secondElf = 1
    
    val recipes = arrayListOf<Int>()
    recipes.ensureCapacity(n)
    recipes.addAll(initialRecipes)
    
    while (recipes.size < n){
        recipes.addAll(newRecipes(Pair(recipes[firstElf], recipes[secondElf])))
        firstElf = (firstElf + 1 + recipes[firstElf]) % recipes.size
        secondElf = (secondElf + 1 + recipes[secondElf]) % recipes.size
    }
    
    return recipes.take(n)
}

fun getScore(recipes: List<Int>, length: Int) : Int =
    Integer.parseInt(recipes.takeLast(length).joinToString(""){Integer.toString(it)})

fun scoresUntil(n: Int, scoreLength: Int): Int {
    
    val recipes = arrayListOf<Int>()
    recipes.addAll(initialRecipes)
    
    var firstElf = 0
    var secondElf = 1
    
    while (true){
        
        val newRecipes = newRecipes(Pair(recipes[firstElf], recipes[secondElf]))
        for (recipe in newRecipes){
            recipes.add(recipe)
            if (recipes.size >= scoreLength){
                val newScore = getScore(recipes, scoreLength)
                if (newScore == n){
                    return recipes.size - scoreLength
                }
            }
        }
        firstElf = (firstElf + 1 + recipes[firstElf]) % recipes.size
        secondElf = (secondElf + 1 + recipes[secondElf]) % recipes.size
        
    }
    
}

private fun first() : String { // Should technically convert it to long, but the output will look the same
    return firstNRecipes(input + 10).takeLast(10).joinToString("") {Integer.toString(it)}
}

private fun second() : Int {
    return scoresUntil(input, 6)
}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}