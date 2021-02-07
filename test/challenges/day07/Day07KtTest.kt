package challenges.day07

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day07KtTest : StringSpec({
    val dependencyStrings = listOf(
        "Step C must be finished before step A can begin.",
        "Step C must be finished before step F can begin.",
        "Step A must be finished before step B can begin.",
        "Step A must be finished before step D can begin.",
        "Step B must be finished before step E can begin.",
        "Step D must be finished before step E can begin.",
        "Step F must be finished before step E can begin."
    )

    "correctOrder returns the first alphabetical possible sequence of steps" {
        correctOrder(dependencyStrings) shouldBe "CABDFE"
    }

    "processingTime returns the minimum amount of time for the given number of workers to complete all tasks" {
        processingTime(dependencyStrings, 2) shouldBe 15
    }
})
