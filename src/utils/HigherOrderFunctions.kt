package utils

/**
 *  Repeatedly apply a function with signature (T) -> T n times on an initial value of type T
 */
inline fun <T> repeat(initial: T, f: (T) -> T, n: Long) : T {

    // Implemented iteratively because recursive functions cannot be inlined
    var value = initial
    for (i in 0 until n){
        value = f(value)
    }
    return value

}
