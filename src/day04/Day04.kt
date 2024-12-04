package day04

import Utils
import getColumns
import getDiagonals

fun main() {
    val utils = Utils(4)

    fun part1(input: List<String>): Int {
        val lines = buildList {
            addAll(input)
            addAll(input.map(String::reversed))

            val verticals = input.getColumns()
            addAll(verticals)
            addAll(verticals.map(String::reversed))

            val diagonals = input.getDiagonals()
            addAll(diagonals)
            addAll(diagonals.map(String::reversed))
        }

        val regex = Regex("XMAS")
        return lines.sumOf { regex.findAll(it).count() }
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (x in input.first().indices) {
            for (y in input.indices) {
                if (input.getOrNull(x + 1)?.getOrNull(y + 1) != 'A') continue
                val diagonal1 = buildString {
                    input.getOrNull(x)?.getOrNull(y)?.let(::append)
                    input.getOrNull(x + 1)?.getOrNull(y + 1)?.let(::append)
                    input.getOrNull(x + 2)?.getOrNull(y + 2)?.let(::append)
                }
                val diagonal2 = buildString {
                    input.getOrNull(x + 2)?.getOrNull(y)?.let(::append)
                    input.getOrNull(x + 1)?.getOrNull(y + 1)?.let(::append)
                    input.getOrNull(x)?.getOrNull(y + 2)?.let(::append)
                }
                if ((diagonal1 == "MAS" || diagonal1.reversed() == "MAS") && (diagonal2 == "MAS" || diagonal2.reversed() == "MAS")) {
                    count++
                }
            }
        }
        return count
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}