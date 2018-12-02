package utils

import java.io.File

/**
 * Functions for reading the line-separated data, with conversion to different data types as necessary
 */

const val dir = "input"

fun importDataStr(day: Int): List<String>{

    val file = File("$dir/$day")
    return file.readLines()

}

fun importDataInt(day: Int): List<Int>{

    val file = File("$dir/$day")
    return file.readLines().map{it.toInt()}

}