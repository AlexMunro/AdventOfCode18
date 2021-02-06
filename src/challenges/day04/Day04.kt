package challenges.day04

import utils.importDataStr
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * The input for this challenge is an unsorted list of date/timestamped events.
 * Each listed day has many guard events pertaining to only one guard.
 */

data class GuardEvent(val dateTime: LocalDateTime, val asleep: Boolean) : Comparable<GuardEvent> {
    override fun compareTo(other: GuardEvent): Int = this.dateTime.compareTo(other.dateTime)
}

data class GuardDate(var guardId: Int = -1, val events: ArrayList<GuardEvent> = ArrayList())

// Begin by building sorted lists of each day's events
private val eventsByDay = {
    val dayMap = HashMap<LocalDate, GuardDate>()
    val input = importDataStr(4)
    // First for date, second for time, third for the event contents
    val timestampRegex = Regex("\\[(\\d{4}\\-\\d{2}\\-\\d{2}) (\\d{2}\\:\\d{2})\\] (.*)")
    val guardIdRegex = Regex("Guard \\#(\\d*) begins shift")

    for (event in input) {
        // Sort each event by shift
        val matchedGroups = timestampRegex.find(event)!!.groups

        val dateTime = LocalDateTime.parse("${matchedGroups[1]!!.value}T${matchedGroups[2]!!.value}")
        // Some guards start slightly before midnight, so consider any shift starting after 23:00 as the next day
        val date = if (dateTime.hour == 23) dateTime.toLocalDate().plusDays(1) else dateTime.toLocalDate()

        if (!dayMap.containsKey(date)) {
            dayMap[date] = GuardDate()
        }

        // Each line either identifies the guard and their start time or indicates a sleep event
        val matchedId = guardIdRegex.find(matchedGroups[3]!!.value)
        if (matchedId != null) {
            dayMap[date]!!.guardId = matchedId.groups[1]!!.value.toInt()
        } else {
            // Guard event starts 'w' for "wakes up" and 'f' for "falls asleep"
            dayMap[date]!!.events.add(GuardEvent(dateTime, matchedGroups[3]!!.value[0] == 'f'))
        }
    }

    dayMap.values.forEach { it.events.sort() }

    // Returning only the values since dates are not relevant beyond grouping
    dayMap.values
}()

// Then group days by guard ID
private val guardHistory = eventsByDay.groupBy { it.guardId }

/**
 * Checks if on a given day the guard has already fallen asleep
 */
private fun asleepAtMidnight(date: GuardDate): Boolean {
    // Determine whether awake from 23:59 the previous night
    val beforeMidnight = date.events.takeWhile { it.dateTime.hour == 23 }
    return if (beforeMidnight.isEmpty()) false else !beforeMidnight.last().asleep
}

/**
 * Returns total number of minutes the guard was asleep for on a particular day
 */
private fun midnightMinsAsleep(date: GuardDate): Int {

    var asleep = asleepAtMidnight(date)

    var minutesAsleep = 0
    var min = 0

    for (event in date.events) {
        if (event.dateTime.hour > 0) {
            break
        }
        val newMin = event.dateTime.minute

        if (asleep) {
            minutesAsleep += newMin - min
        }
        min = newMin
        asleep = event.asleep
    }

    // Handle minutes after last event before 01:00
    if (asleep) {
        minutesAsleep += 60 - min
    }
    return minutesAsleep
}

/**
 * Finds which moment was most often slept for. Returns the first such minute if there is a tie with its probability
 */
private fun sleepiestMinute(dates: List<GuardDate>): Pair<Int, Double> {

    val sleepProbabilities = HashMap<Int, Double>()

    // Iterate through each minute of each date and increase the recorded probability by 1/N when asleep
    for (date in dates) {

        var asleep = asleepAtMidnight(date)
        var min = 0

        for (event in date.events) {
            if (event.dateTime.hour > 0) {
                break
            }
            val newMin = event.dateTime.minute

            if (asleep) {
                (min until newMin).forEach {
                    sleepProbabilities[it] = (sleepProbabilities[it] ?: 0.0) + (1.0 / dates.size)
                }
            }
            min = newMin
            asleep = event.asleep
        }

        // Handle minutes after last event before 01:00
        if (asleep) {
            (min until 60).forEach {
                sleepProbabilities[it] = (sleepProbabilities[it] ?: 0.0) + (1.0 / dates.size)
            }
        }

    }

    // Find and return highest value in the sleep probability map
    var sleepiestMinute = -1
    var maxProb = 0.0
    for ((min, prob) in sleepProbabilities) {
        if (prob > maxProb) {
            sleepiestMinute = min
            maxProb = prob
        }
    }
    return Pair(sleepiestMinute, maxProb)
}

/**
 * Returns the ID of the guard who sleeps the most multiplied by the minute of the midnight hour they sleep most for
 */
private fun first(): Int {

    // Find the guard who has slept the longest

    var sleepiestGuardId = -1
    var mostMinutesSlept = -1

    for ((guardId, dates) in guardHistory) {
        val sleepAmount = dates.map { midnightMinsAsleep(it) }.sum()
        if (sleepAmount > mostMinutesSlept) {
            mostMinutesSlept = sleepAmount
            sleepiestGuardId = guardId
        }
    }

    // Return the minute that guard is most often asleep multiplied by their ID
    return sleepiestMinute(guardHistory[sleepiestGuardId]!!).first * sleepiestGuardId
}

/**
 * Returns the ID of the guard who sleeps most on a particular minute multiplied by that minute of the midnight hour
 */
private fun second(): Int {
    val sleepiestGuardMinute = guardHistory.keys.map { guard -> Pair(guard, sleepiestMinute(guardHistory[guard]!!)) }
        .maxBy { it.second.second }
    return sleepiestGuardMinute!!.first * sleepiestGuardMinute!!.second!!.first
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}