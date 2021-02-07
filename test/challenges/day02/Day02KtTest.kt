package challenges.day02

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day02KtTest : StringSpec({
    "checksum multiplies the numbers of strings with duplicate and triplicate characters" {
        val ids = listOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")
        checksum(ids) shouldBe 12
    }

    "commonLetters returns the letters in common of Hamming adjacent strings" {
        val ids = listOf("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")
    }
})
