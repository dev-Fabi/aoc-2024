package day03

import Utils

fun main() {
    val utils = Utils(3)

    val instructionRegex = Regex("""mul\((\d+),(\d+)\)""")
    val conditionalRegex = Regex("""do\(\)|don't\(\)|$instructionRegex""")

    fun part1(input: String): Long {
        return instructionRegex.findAll(input).sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }
    }

    fun part2(input: String): Long {
        var enabled = true
        return conditionalRegex.findAll(input).sumOf {
            return@sumOf when (it.value) {
                "do()" -> {
                    enabled = true
                    0
                }

                "don't()" -> {
                    enabled = false
                    0
                }

                else -> if (enabled) {
                    it.groupValues[1].toLong() * it.groupValues[2].toLong()
                } else {
                    0
                }
            }
        }
    }

    // Test if implementation meets criteria from the description
    check(part1(utils.readFile("test")) == 161L)
    check(part2(utils.readFile("test2")) == 48L)

    // Solve puzzle and print result
    val input = utils.readFile().replace("\n", "")
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}