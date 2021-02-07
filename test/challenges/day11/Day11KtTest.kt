package challenges.day11

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day11KtTest : StringSpec({
    "mostPowerful3By3 returns the co-ordinates of the most powerful 3x3 square" {
        mostPowerful3By3(18) shouldBe Pair(33, 45)
        mostPowerful3By3(42) shouldBe Pair(21, 61)
    }

    "mostPowerfulSquare returns the co-ordinates and size of the most powerful square" {
        mostPowerfulSquare(18) shouldBe Triple(90, 269, 16)
        mostPowerfulSquare(42) shouldBe Triple(232, 251, 12)
    }
})
