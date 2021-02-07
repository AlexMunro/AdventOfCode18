package challenges.day09

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day09KtTest : StringSpec({
    "maxScore returns the winning score from a game" {
        maxScore("10 players; last marble is worth 1618 points") shouldBe 8317
        maxScore("13 players; last marble is worth 7999 points") shouldBe 146373
        maxScore("17 players; last marble is worth 1104 points") shouldBe 2764
        maxScore("21 players; last marble is worth 6111 points") shouldBe 54718
        maxScore("30 players; last marble is worth 5807 points") shouldBe 37305
    }

    "maxScoreEmbiggened returns the max score if the number of the winning marble was 100 times larger" {
        maxScoreEmbiggened("10 players; last marble is worth 1618 points") shouldBe 74765078
    }
})
