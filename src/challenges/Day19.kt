package challenges

import utils.importDataStr

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
    return executeWithInstrReg(arrayOf(1,0,0,0,0,0))[0]
}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}
