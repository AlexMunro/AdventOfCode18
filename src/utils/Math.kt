package utils

import kotlin.math.sqrt

fun variance(vals: Collection<Int>): Double {
    return vals.map { it - (vals.average()) }.sum() / vals.size
}

fun longVariance(vals: Collection<Long>): Double {
    vals.average()
    return vals.map { it - (vals.average()) }.sum() / vals.size
}

fun stdDev(vals: Collection<Int>): Double {
    return sqrt(variance(vals))
}
