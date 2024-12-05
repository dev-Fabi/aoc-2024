import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.max

class Utils(val day: Int) {
    private val dayString: String = day.toString().padStart(2, '0')

    /**
     * Reads lines from the given input txt file.
     */
    fun readLines(suffix: String? = null) = getFile(suffix).readLines()

    /**
     * Reads the complete text from given input file.
     */
    fun readFile(suffix: String? = null) = getFile(suffix).readText()

    private fun getFile(suffix: String?): File {
        val fileName = StringBuilder("Day${dayString}")
        suffix?.let { fileName.append("_$suffix") }
        fileName.append(".txt")

        return File("src/day$dayString", fileName.toString())
    }
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Converts string list to int list
 */
fun List<String>.asInt(): List<Int> = map { it.toInt() }

/**
 * Returns a progression from this value up/down to the specified to value.
 * Including both, this and to value
 */
infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

/**
 * Performs the given action on each element of a 2D list, providing sequential index with the element.
 * @param action function that takes the row and column index (as pair) of an element and the element itself and performs the action on the element
 */
fun <T> List<List<T>>.forEach2D(action: (index: Pair<Int, Int>, element: T) -> Unit) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, element ->
            action(rowIndex to columnIndex, element)
        }
    }
}

/**
 * Increases the value of the key in a map
 */
fun <K> MutableMap<K, Int>.increase(key: K, by: Int = 1) = set(key, getOrDefault(key, 0).plus(by))

/**
 * Increases the value of the key in a map
 */
fun <K> MutableMap<K, Long>.increase(key: K, by: Long = 1) = set(key, getOrDefault(key, 0).plus(by))

infix fun Int.divisibleBy(other: Int): Boolean = this % other == 0
infix fun Long.divisibleBy(other: Long): Boolean = this % other == 0L

fun Int.nextIndexOf(collection: Collection<*>): Int = (this + 1) % collection.size
fun Int.nextIndexOf(collection: CharSequence): Int = (this + 1) % collection.length

fun <T> List<T>.asInfiniteLoop(): Iterator<T> {
    var index = 0
    return generateSequence {
        val next = this[index]
        index = index.nextIndexOf(this)
        return@generateSequence next
    }.iterator()
}

fun leastCommonMultiply(a: Long, b: Long): Long {
    val larger = max(a, b)
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm.divisibleBy(a) && lcm.divisibleBy(b)) return lcm
        lcm += larger
    }
    return maxLcm
}

fun List<Int>.leastCommonMultiply(): Long = this.map(Int::toLong).reduce { acc, i ->
    leastCommonMultiply(acc, i)
}

fun <T> MutableList<T>.addNotNull(item: T?): Boolean {
    if (item == null) return false
    return add(item)
}

fun List<String>.getDiagonals(): List<String> {
    val fDiagonals = mutableMapOf<Int, StringBuilder>()
    val bDiagonals = mutableMapOf<Int, StringBuilder>()
    for (x in this.first().indices) {
        for (y in this.indices) {
            val current = this[y][x]
            fDiagonals.getOrPut(x + y, ::StringBuilder).append(current)
            bDiagonals.getOrPut(x - y, ::StringBuilder).append(current)
        }
    }
    return fDiagonals.values.map(StringBuilder::toString) + bDiagonals.values.map(StringBuilder::toString)
}

fun List<String>.getColumns(): List<String> = buildList {
    for (x in this@getColumns.first().indices) {
        val column = buildString {
            for (y in this@getColumns.indices) {
                append(this@getColumns[y][x])
            }
        }
        add(column)
    }
}

fun <T> List<T>.strictMiddle(): T {
    val count = count()
    if (count % 2 == 0) error("Collection has no middle element")
    return this[count / 2]
}