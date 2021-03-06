package challenges.day08

import utils.importDataStr

/**
 * Constructs a node from a string of numbers with the form:
 *     - 1 Header
 *         Number of child nodes
 *         Number of metadata entries
 *     - 0+ child nodes (recursively using this structure)
 *     - 1+ metadata entries
 */

/**
 * Sub-graph of a single node and all its descendants
 */
class Node(representation: List<Int>) {
    private val children: List<Node>
    private val metadata: List<Int>
    private val length: Int

    init {
        val childrenArrayList = arrayListOf<Node>()
        var currentLength = 2 // Starting after header
        // Parse each child - total length will not be known ahead of time due to recursive children
        for (i in 0 until representation[0]) {
            val child = Node(representation.drop(currentLength))
            childrenArrayList.add(child)
            currentLength += child.length
        }
        // Add all metadata
        metadata = representation.slice(currentLength until currentLength + representation[1])
        currentLength += metadata.size
        children = childrenArrayList
        length = currentLength
    }

    fun sumMetadata(): Int = metadata.sum() + children.map { it.sumMetadata() }.sum()

    fun value(): Int {
        return if (children.isEmpty()) {
            metadata.sum()
        } else {
            // metadata is one-indexed smh
            metadata.map { (children.getOrNull(it - 1)?.value()) ?: 0 }.sum()
        }
    }

}

fun sumMetadata(file: List<Int>): Int = Node(file).sumMetadata()

fun rootValue(file: List<Int>): Int = Node(file).value()

fun main() {
    val input = importDataStr(8)[0].split(" ").map { it.toInt() }

    println("First solution: ${sumMetadata(input)}")
    println("Second solution: ${rootValue(input)}")
}