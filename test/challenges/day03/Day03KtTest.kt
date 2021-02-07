package challenges.day03

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day03KtTest : StringSpec({
    val claims = listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")

    "overlappedPoints returns the number of points covered by more than one rectangle" {
        overlappedPoints(claims) shouldBe 4
    }

    "nonOverlappingRectangle returns the ID of the only rectangle that doesn't overlap with any other" {
        nonOverlappingRectangle(claims) shouldBe 3
    }
})
