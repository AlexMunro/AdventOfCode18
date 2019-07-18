package challenges

import utils.importDataStr
import java.lang.IndexOutOfBoundsException

private val input = importDataStr(16)


data class DataSample(val a: Int, val b: Int, val c: Int, val r1: Array<Int>, val r2: Array<Int>)

private val dataSamples: List<DataSample> = {
    var i = 0
    val samples = mutableListOf<DataSample>()
    
    while(input[i].startsWith("Before: ")){
        
        fun getArray(line: String): Array<Int> {
            return Regex("""\[((?:\d+, )*\d+)\]""")
                .find(line)!!.groupValues[1]
                .split(", ")    
                .map{ it.toInt() }
                .toTypedArray()
        }
        
        val r1 = getArray(input[i])
        i++
        
        val (_, a, b, c) = input[i].split(" ").map{ it.toInt() }
        i++
        
        val r2 = getArray(input[i])
        i += 2
        
        samples.add(DataSample(a, b, c, r1, r2))
    }
    
    samples
}()

typealias OpCode = (Int, Int, Int, Array<Int>) -> Array<Int>

val opCodes: Map<String, OpCode> = mapOf(

    // Addition
    "addr" to { a, b, c, r -> r.clone().also { it[c] = r[a] + r[b] } },
    "addi" to { a, b, c, r -> r.clone().also { it[c] = r[a] + b } },
            
    // Multiplication 
    "mulr" to { a, b, c, r -> r.clone().also{ it[c] = r[a] * r[b] } },
    "muli" to { a, b, c, r -> r.clone().also{ it[c] = r[a] * b } },
    
    // Bitwise AND
    "banr" to { a, b, c, r -> r.clone().also{ it[c] = r[a] and r[b] } },
    "bani" to { a, b, c, r -> r.clone().also{ it[c] = r[a] and b } },
    
    // Bitwise OR
    "borr" to { a, b, c, r -> r.clone().also{ it[c] = r[a] or r[b] } },
    "bori" to { a, b, c, r -> r.clone().also{ it[c] = r[a] or b } },

    // Assignment
    "setr" to { a, _, c, r -> r.clone().also{ it[c] = r[a] } },
    "seti" to { a, _, c, r -> r.clone().also{ it[c] = a } },

    // Greater-than testing
    
    "gtir" to { a, b, c, r -> r.clone().also{ it[c] = if (a > r[b]) 1 else 0 } },
    "gtri" to { a, b, c, r -> r.clone().also{ it[c] = if (r[a] > b) 1 else 0 } },
    "gtrr" to { a, b, c, r -> r.clone().also {it[c] = if (r[a] > r[b]) 1 else 0 } },

    // Equality testing
    "eqir" to { a, b, c, r -> r.clone().also{ it[c] = if (a == r[b]) 1 else 0 } },
    "eqri" to { a, b, c, r -> r.clone().also{ it[c] = if (r[a] == b) 1 else 0 } },
    "eqrr" to { a, b, c, r -> r.clone().also{ it[c] = if (r[a] == r[b]) 1 else 0 } }
)

private fun first(): Int {
    return dataSamples.count{ (a, b, c, r1, r2) -> 
        opCodes.count { 
            try { it.value.invoke(a, b, c, r1).contentEquals(r2) } 
            catch (e: IndexOutOfBoundsException) { false }
        } >= 3
    }
}

private fun second() {

}

fun main(args: Array<String>) {
    println("First solution: ${first()}")
    println("Second solution: ${second()}")
}