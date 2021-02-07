package challenges.day13

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day13KtTest : StringSpec({
    "nextCrashLoc finds the location of the next crash" {
        var input = listOf(
            """/->-\""",
            """|   |  /----\""",
            """| /-+--+-\  |""",
            """| | |  | v  |""",
            """\-+-/  \-+--/""",
            """\------/""",
        )

        var (track, carts) = processInput(input)
        nextCrashLoc(track, carts) shouldBe Pair(7, 3)
    }

    "lastCarLoc finds the location of the last remaining car after the final crash" {
        var input = listOf(
            """/>-<\  """,
            """|   |  """,
            """| /<+-\""",
            """| | | v""",
            """\>+</ |""",
            """  |   ^""",
            """  \<->/""",
        )

        var (track, carts) = processInput(input)
        lastCarLoc(track, carts) shouldBe Pair(6, 4)
    }
})
