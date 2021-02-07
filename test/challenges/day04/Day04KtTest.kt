package challenges.day04

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day04KtTest : StringSpec({
    val sleepLog = listOf(
        "[1518-11-01 00:00] Guard #10 begins shift",
        "[1518-11-01 00:05] falls asleep",
        "[1518-11-01 00:25] wakes up",
        "[1518-11-01 00:30] falls asleep",
        "[1518-11-01 00:55] wakes up",
        "[1518-11-01 23:58] Guard #99 begins shift",
        "[1518-11-02 00:40] falls asleep",
        "[1518-11-02 00:50] wakes up",
        "[1518-11-03 00:05] Guard #10 begins shift",
        "[1518-11-03 00:24] falls asleep",
        "[1518-11-03 00:29] wakes up",
        "[1518-11-04 00:02] Guard #99 begins shift",
        "[1518-11-04 00:36] falls asleep",
        "[1518-11-04 00:46] wakes up",
        "[1518-11-05 00:03] Guard #99 begins shift",
        "[1518-11-05 00:45] falls asleep",
        "[1518-11-05 00:55] wakes up"
    )

    "sleepiestGuard multiplies the ID of the sleepiest guard by the minute they most frequently sleep" {
        sleepiestGuard(sleepLog) shouldBe 240
    }

    "sleepiestMinuteProduct multiplies the guard who sleeps the most on a given minute by that minute" {
        sleepiestMinuteProduct(sleepLog) shouldBe 4455
    }
})
