package challenges.day19

import utils.importDataStr

val operations: Map<String, Operation> = mapOf(

    // Addition
    "addr" to { a, b, c, r -> r.clone().also { it[c] = r[a] + r[b] } },
    "addi" to { a, b, c, r -> r.clone().also { it[c] = r[a] + b } },

    // Multiplication
    "mulr" to { a, b, c, r -> r.clone().also { it[c] = r[a] * r[b] } },
    "muli" to { a, b, c, r -> r.clone().also { it[c] = r[a] * b } },

    // Bitwise AND
    "banr" to { a, b, c, r -> r.clone().also { it[c] = r[a] and r[b] } },
    "bani" to { a, b, c, r -> r.clone().also { it[c] = r[a] and b } },

    // Bitwise OR
    "borr" to { a, b, c, r -> r.clone().also { it[c] = r[a] or r[b] } },
    "bori" to { a, b, c, r -> r.clone().also { it[c] = r[a] or b } },

    // Assignment
    "setr" to { a, _, c, r -> r.clone().also { it[c] = r[a] } },
    "seti" to { a, _, c, r -> r.clone().also { it[c] = a } },

    // Greater-than testing

    "gtir" to { a, b, c, r -> r.clone().also { it[c] = if (a > r[b]) 1 else 0 } },
    "gtri" to { a, b, c, r -> r.clone().also { it[c] = if (r[a] > b) 1 else 0 } },
    "gtrr" to { a, b, c, r -> r.clone().also { it[c] = if (r[a] > r[b]) 1 else 0 } },

    // Equality testing
    "eqir" to { a, b, c, r -> r.clone().also { it[c] = if (a == r[b]) 1 else 0 } },
    "eqri" to { a, b, c, r -> r.clone().also { it[c] = if (r[a] == b) 1 else 0 } },
    "eqrr" to { a, b, c, r -> r.clone().also { it[c] = if (r[a] == r[b]) 1 else 0 } }
)


private val input = importDataStr(19)

val instrReg = Regex("""#ip (\d)""").find(input[0])!!.groupValues[1].toInt()

val instructionSet = input.drop(1).map { Regex("""([a-z]+) (\d+) (\d+) (\d+)""").find(it)!!.groupValues }

typealias Operation = (Int, Int, Int, Array<Int>) -> Array<Int>

private tailrec fun executeWithInstrReg(registers: Array<Int>): Array<Int> {
    if (registers[instrReg] >= instructionSet.size) return registers

    val instruction = instructionSet[registers[instrReg]]

    val fn: Operation = operations[instruction[1]]!!
    val (a, b, c) = instruction.subList(2, 5).map { it.toInt() }

    return executeWithInstrReg(fn(a, b, c, registers).also { it[instrReg] = it[instrReg] + 1 })
}

private fun first(): Int {
    return executeWithInstrReg(arrayOf(0, 0, 0, 0, 0, 0))[0]
}

private fun second(): Int {
    // Unfortunately, this one is specific to the input provided. Sorry.
    // It was obtained by reverse engineering the bytecode given,
    // discovering the problem it was trying to solve, and optimising.
    val b = 10_551_370
    return (1..b).filter { b % it == 0 }.sum()
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}
