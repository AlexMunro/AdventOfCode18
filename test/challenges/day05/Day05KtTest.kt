package challenges.day05

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class Day05KtTest : StringSpec({

    "getConsolidatedSize returns the length of a string after repeatedly eliminating opposite polarity neighbours" {
        getConsolidatedSize("aA") shouldBe 0
        getConsolidatedSize("abBA") shouldBe 0
        getConsolidatedSize("abAB") shouldBe 4
        getConsolidatedSize("aabAAB") shouldBe 6
        getConsolidatedSize("dabAcCaCBAcCcaDA") shouldBe 10
    }

    "filteredPolymer returns the shorted possible length of string after removing all of any letter pair and consolidating" {
        filteredPolymer("dabAcCaCBAcCcaDA") shouldBe 4
    }
})
