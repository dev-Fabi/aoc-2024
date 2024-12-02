package day01

import Utils
import kotlin.math.abs

fun main() {
    val utils = Utils(1)

    fun List<String>.getLocationIdLists(): Pair<List<Int>, List<Int>> {
        val rightNumbers = mutableListOf<Int>()
        val leftNumbers = mutableListOf<Int>()
        this.forEach {
            val parts = it.split("   ")
            leftNumbers.add(parts.component1().toInt())
            rightNumbers.add(parts.component2().toInt())
        }

        return leftNumbers to rightNumbers
    }

    fun part1(input: List<String>): Long {
        val (leftNumbers, rightNumbers) = input.getLocationIdLists()

        return rightNumbers.sorted().zip(leftNumbers.sorted()) { r, l -> abs(r - l).toLong() }.sum()
    }

    fun part2(input: List<String>): Long {
        val (leftNumbers, rightNumbers) = input.getLocationIdLists()

        val rightOccurrenceMap = buildMap<Int, Int> {
            rightNumbers.forEach {
                compute(it) { _, current -> (current ?: 0) + 1 }
            }
        }

        return leftNumbers.sumOf { it * rightOccurrenceMap.getOrDefault(it, 0).toLong() }
    }

    // Test if implementation meets criteria from the description
    check(part1(utils.readLines("test")) == 11L)
    check(part2(utils.readLines("test")) == 31L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}