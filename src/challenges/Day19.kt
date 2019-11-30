package challenges

import utils.importDataStr
import java.lang.Math.floor
import kotlin.math.sqrt

// Uses the value operations from Day16

private val input = importDataStr(19)

val instrReg = Regex("""#ip (\d)""").find(input[0])!!.groupValues[1].toInt()

val instructionSet = input.drop(1).map{ Regex("""([a-z]+) (\d+) (\d+) (\d+)""").find(it)!!.groupValues }

private tailrec fun executeWithInstrReg(registers: Array<Int>): Array<Int>{
    if (registers[instrReg] >= instructionSet.size) return registers
    
    val instruction = instructionSet[registers[instrReg]]
    
    val fn: Operation = operations[instruction[1]]!!
    val (a, b, c) = instruction.subList(2, 5).map{ it.toInt() }
    
    return executeWithInstrReg(fn(a, b, c, registers).also{ it[instrReg] = it[instrReg] + 1 })
}

private fun first(): Int {
    return executeWithInstrReg(arrayOf(0,0,0,0,0,0))[0]
}

private fun second(): Int {
    // Unfortunately, this one is specific to the input provided. Sorry.
    // It was obtained by reverse engineering the bytecode given,
    // discovering the problem it was trying to solve, and optimising.
    val b = 10_551_370
    return (1..b).filter{ b % it == 0 }.sum()
}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}
