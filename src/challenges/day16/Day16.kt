package challenges.day16

import utils.importDataStr

private val input = importDataStr(16)

data class DataSample(val instruction: Instruction, val r1: Array<Int>, val r2: Array<Int>)
data class Instruction(val opCode: Int, val a: Int, val b: Int, val c: Int)

fun getArray(line: String): Array<Int> {
    return Regex("""\[((?:\d+, )*\d+)\]""")
        .find(line)!!.groupValues[1]
        .split(", ")
        .map { it.toInt() }
        .toTypedArray()
}

private val dataSamples: List<DataSample> = {
    var i = 0
    val samples = mutableListOf<DataSample>()

    while (input[i].startsWith("Before: ")) {
        val r1 = getArray(input[i])
        i++

        val (opCode, a, b, c) = input[i].split(" ").map { it.toInt() }
        i++

        val r2 = getArray(input[i])
        i += 2

        samples.add(DataSample(Instruction(opCode, a, b, c), r1, r2))
    }

    samples
}()

private val instructions: List<Instruction> = {
    var i = 0
    while (input[i].startsWith("Before: ")) {
        i += 4
    }
    i += 2

    input.takeLast(input.size - i).map {
        val (opCode, a, b, c) = it.split(" ").map { it.toInt() }
        Instruction(opCode, a, b, c)
    }
}()

typealias Operation = (Int, Int, Int, Array<Int>) -> Array<Int>

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

val mapOpcodes: Map<Int, Operation> = {
    val remainingOperations = operations.toMutableMap()
    var remainingSamples = dataSamples
    val opcodeMap = mutableMapOf<Int, Operation>()

    while (remainingOperations.isNotEmpty()) {

        val assignableOperations: Map<Int, Map<String, Operation>> = remainingSamples.map { (i, r1, r2) ->
            i.opCode to remainingOperations.filter {
                try {
                    it.value.invoke(i.a, i.b, i.c, r1).contentEquals(r2)
                } catch (e: IndexOutOfBoundsException) {
                    false
                }
            }
        }.filter { it.second.size == 1 }.toMap()

        if (assignableOperations.isEmpty())
            throw Exception("Unable to assign all opcodes")

        assignableOperations.forEach { op ->
            opcodeMap[op.key] = op.value.values.first()
            remainingOperations.remove(op.value.keys.first())
            remainingSamples = remainingSamples.filterNot { it.instruction.opCode == op.key }
        }
    }

    opcodeMap
}()

fun execute(initialRegisters: Array<Int>): Array<Int> {
    var registers = initialRegisters
    instructions.forEach {
        val operation = mapOpcodes[it.opCode] ?: error("Operation not found")
        registers = operation.invoke(it.a, it.b, it.c, registers)
    }
    return registers
}

private fun first(): Int {
    return dataSamples.count { (i, r1, r2) ->
        operations.count {
            try {
                it.value.invoke(i.a, i.b, i.c, r1).contentEquals(r2)
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        } >= 3
    }
}

private fun second(): Int {
    return execute(arrayOf(0, 0, 0, 0))[0]
}

fun main() {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}
