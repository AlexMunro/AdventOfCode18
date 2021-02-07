package challenges.day02

import utils.importDataStr

/**
 * Return the product of number of strings with exactly 2 occurrences of any character with that of 3 occurrences
 */
fun checksum(ids: List<String>): Int {
    var doubleCount = 0
    var tripleCount = 0

    ids.forEach { string ->
        val charMap = HashMap<Char, Int>() // Map of how many times each character appears

        string.forEach {
            charMap[it] = (charMap[it] ?: 0) + 1
        }

        if (2 in charMap.values) {
            doubleCount++
        }

        if (3 in charMap.values) {
            tripleCount++
        }
    }

    return doubleCount * tripleCount
}

/**
 * Given that only two of the set of strings have a Hamming distance of one,
 * return the string resulting from deleting the differing character.
 */
fun commonLetters(ids: List<String>): String {
    /**
     * If two strings have a Hamming distance of one, return the index of the difference, else return null.
     */
    fun findSingleDifference(str1: String, str2: String): Int? {
        var charDiffIdx: Int? = null // Non-null once a single difference has been found

        // Comparing each char
        str1.indices.forEach {
            if (str1[it] != str2[it]) {
                if (charDiffIdx != null) // Implies Hamming distance >= 2
                    return null
                else
                    charDiffIdx = it
            }
        }
        return charDiffIdx // Remains null if no differences are found
    }

    for (i in 0 until (ids.size - 1)) {
        for (j in i + 1 until ids.size) {
            val singleDiff = findSingleDifference(ids[i], ids[j])
            if (singleDiff != null) {
                // Returns the string, minus the single differing character
                return ids[i].slice(0 until singleDiff) + ids[i].slice((singleDiff + 1) until ids[i].length)
            }
        }
    }
    throw Exception("No two strings from the input have a Hamming distance of 1.")
}

fun main() {
    val input = importDataStr(2)

    println("First solution: ${checksum(input)}")
    println("Second solution: ${commonLetters(input)}")
}