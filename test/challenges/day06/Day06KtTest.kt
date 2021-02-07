package challenges.day06

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day06KtTest : StringSpec({
    val coordStrings = listOf("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9")

    "largestArea should return the size of the largest non-infinite region" {
        largestArea(coordStrings) shouldBe 17
    }

    "closePoints should return the number of points within the threshold of all other points" {
        closePoints(coordStrings, 32) shouldBe 16
    }
})
