package challenges.day01

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day01KtTest : StringSpec({
   "sum adds a list of numbers" {
       sum(listOf(1,1,1)) shouldBe 3
       sum(listOf(1,1,-2)) shouldBe 0
       sum(listOf(-1,-2,-3)) shouldBe -6
   }

    "firstDuplicateSum loops until the cumulative sum has been repeated" {
        firstDuplicateSum(listOf(1, -1)) shouldBe 0
        firstDuplicateSum(listOf(3, 3, 4, -2, -4)) shouldBe 10
        firstDuplicateSum(listOf(-6,3,8,5,-6)) shouldBe 5
        firstDuplicateSum(listOf(7,7,-2,-7,-4)) shouldBe 14
    }
})