package challenges.day14

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day14KtTest : StringSpec({
    "next10Scores prints the next 10 scores after the given recipe concatenated" {
        next10Scores(5) shouldBe "0124515891"
        next10Scores(18) shouldBe "9251071085"
        next10Scores(2018) shouldBe "5941429882"
    }

    "scoresUntil prints the number of score before the given recipe score" {
        scoresUntil(51589, 5) shouldBe 9
        scoresUntil(1245, 5) shouldBe 5
        scoresUntil(92510, 5) shouldBe 18
        scoresUntil(59414, 5) shouldBe 2018
    }
})
