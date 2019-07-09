package challenges

import utils.importDataStr

private val input = importDataStr(16)


data class DataSample(val opCode: Int, val a: Int, val b: Int, val c: Int, val r1: List<Int>, val r2: List<Int>)

private val dataSamples: List<DataSample> = {
    var i = 0
    val samples = mutableListOf<DataSample>()
    
    while(input[i].startsWith("Before: ")){
        
        fun getArray(line: String): List<Int> {
            return Regex("""\[((?:\d+, )*\d+)\]""")
                .find(line)!!.groupValues[1]
                .split(", ")    
                .map{ it.toInt() }
        }
        
        val r1 = getArray(input[i])
        i++
        
        val (opCode, a, b, c) = input[i].split(" ").map{ it.toInt() }
        i++
        
        val r2 = getArray(input[i])
        i += 2
        
        samples.add(DataSample(opCode, a, b, c, r1, r2))
    }
    
    samples
}()

fun invalidIndices(xs: List<Any>, vararg i: Int): Boolean {
    return i.any { xs.size < it }
}


val opCodes: List<(DataSample) -> Boolean> = listOf(

    // Addition

    // addr
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, b, c)) false else r1[a] + r1[b] == r2[c] },
    // addi
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else r1[a] + b == r2[c] },

    // Multiplication

    // mulr
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, b, c)) false else r1[a] * r1[b] == r2[c]},
    // muli
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else r1[a] * b == r2[c]},

    // Bitwise AND
    
    // banr
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, b, c)) false else r1[a] and r1[b] == r2[c]},
    // bani
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else r1[a] and b == r2[c]},

    // Bitwise OR
    
    // borr
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, b, c)) false else r1[a] or r1[b] == r2[c]},
    // bori
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else r1[a] or b == r2[c]},

    // Assignment
    
    // setr
    { (_, a, _, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else r1[a] == r2[c]},
    // seti
    { (_, a, _, c, _, r2) -> if (invalidIndices(r2, c)) false else a == r2[c]},

    // Greater-than testing
    
    // gtir
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, b, c)) false else (if (a > r1[b]) r2[c] == 1 else r2[c] == 0) },
    // gtri
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else (if (r1[a] > b) r2[c] == 1 else r2[c] == 0) },
    // gtrr
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, b, c)) false else (if (r1[a] > r1[b] ) r2[c] == 1 else r2[c] == 0) },

    // Equality testing

    // eqir
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, b, c)) false else (if (a == r1[b]) r2[c] == 1 else r2[c] == 0) },
    // eqri
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, c)) false else (if (r1[a] == b) r2[c] == 1 else r2[c] == 0) },
    // eqrr
    { (_, a, b, c, r1, r2) -> if (invalidIndices(r1, a, b, c)) false else (if (r1[a] == r1[b] ) r2[c] == 1 else r2[c] == 0) }
)

private fun first(): Int {
    return dataSamples
        .count{ sample -> (opCodes.count { it.invoke(sample) } >= 3) }
}

private fun second() {

}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}