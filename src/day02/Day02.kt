package day02

import Utils
import kotlin.math.abs

fun main() {
    val utils = Utils(2)

    fun List<Int>.isSafe(): Boolean {
        val direction = this.component1() < this.component2()
        zipWithNext().forEach { (prev, next) ->
            if (direction != (prev < next)) return false
            if (abs(prev - next) !in 1..3) return false
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val reports = input.map { it.split(" ").map(String::toInt) }

        return reports.count { it.isSafe() }
    }

    fun part2(input: List<String>): Int {
        val reports = input.map { it.split(" ").map(String::toInt) }
        val variants = reports.map {
            buildList {
                for (i in it.indices) {
                    add(it.take(i) + it.drop(i + 1))
                }
            }
        }

        return variants.count { it.any(List<Int>::isSafe) }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}