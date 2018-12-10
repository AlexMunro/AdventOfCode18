package utils

/**
 * Mutable circular doubly linked list with changeable _head position. By default, first inserted element is _head.
 */
class DoublyLinkedList<T> {

    var size = 0
    private var _head: Node<T>? = null

    /**
     * Stores a single value in the list with links to the previous and next nodes
     */
    private class Node<T> {
        val value: T
        var prev: Node<T>
        var next: Node<T>

        constructor (value: T) {
            this.value = value
            this.prev = this
            this.next = this
        }

        constructor (value: T, prev: Node<T>, next: Node<T>) {
            this.value = value
            this.prev = prev
            this.next = next
        }

    }

    val head get() = _head?.value
    val isEmpty get() = size == 0


    /**
     * Inserts a node immediately after the current _head. Does not change the _head.
     */
    fun insertNext(value: T) {
        if (size == 0) {
            _head = Node(value)
        } else {
            val node = Node(value, _head!!, _head!!.next)
            this._head!!.next.prev = node
            this._head!!.next = node
        }
        this.size += 1
    }

    /**
     * Inserts a node immediately before the current _head. Does not change the _head.
     */
    fun insertPrev(value: T) {
        if (size == 0) {
            _head = Node(value)
        } else {
            val node = Node(value, _head!!.prev, _head!!)
            this._head!!.prev.next = node
            this._head!!.prev = node
        }
        this.size += 1
    }

    /**
     * Inserts a node immediately after the current _head and changes the _head to this new value
     */
    fun insertNextAndShift(value: T) {
        this.insertNext(value)
        this.moveHead(1)
    }

    fun insertPrevAndShift(value: T) {
        this.insertPrev(value)
        this.moveHead(-1)
    }


    /**
     * Removes the current _head and changes it to the next _head. Returns the removed head's value.
     */
    fun remove(): T {
        if (isEmpty) {
            throw Exception("The list is already empty")
        } else {
            val removedVal = _head!!.value
            if (size == 1) {
                _head = null
            } else {
                _head!!.prev.next = _head!!.next
                _head!!.next.prev = _head!!.prev
                _head = _head!!.next
            }
            size -= 1
            return removedVal
        }
    }

    /**
     * Moves the _head n positions forwards. Negative n moves the _head backwards.
     */
    tailrec fun moveHead(n: Int) {
        if (_head == null) {
            return // Don't need to do anything with an empty list, but no need to throw an exception either
        }
        if (n > 0) {
            this._head = _head!!.next
            moveHead(n - 1)
        } else if (n < 0) {
            this._head = _head!!.prev
            moveHead(n + 1)
        }
    }

}

/**
 * Converts a list of elements into a doubly linked list, with the first as the head
 */
fun <T> doublyLinkedListOf(vararg elements: T): DoublyLinkedList<T> {
    val list = DoublyLinkedList<T>()
    elements.forEach { list.insertNextAndShift(it) }
    return list
}
