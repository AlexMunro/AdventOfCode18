package challenges.day08

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day08KtTest : StringSpec({
    val file = listOf(2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2)

    "sumMetadata adds up all metadata entries" {
        sumMetadata(file) shouldBe 138
    }

    "rootValue returns the value of the root node" {
        rootValue(file) shouldBe 66
    }
})
