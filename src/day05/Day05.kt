package day05

import Utils
import strictMiddle

fun main() {
    val utils = Utils(5)

    fun String.getRulesAndUpdates(): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val (orderRuleSection, updateSection) = split("\n\n")
        val orderRules = orderRuleSection.lines().map { rule ->
            rule.split("|").map(String::toInt).let { it.component1() to it.component2() }
        }
        val updates = updateSection.lines().map { it.split(",").map(String::toInt) }

        return orderRules to updates
    }

    fun List<List<Int>>.partitionUpdates(rules: List<Pair<Int, Int>>): Pair<List<List<Int>>, List<List<Int>>> {
        return this.partition { update ->
            val applyingRules = rules.filter { it.first in update && it.second in update }
            if (applyingRules.isEmpty()) return@partition true

            val pageNumberToIndex = update.withIndex().associate { it.value to it.index }
            return@partition applyingRules.all { (lower, higher) ->
                pageNumberToIndex[lower]!! < pageNumberToIndex[higher]!!
            }
        }
    }

    fun part1(input: String): Int {
        val (orderRules, updates) = input.getRulesAndUpdates()
        val (correctUpdates, _) = updates.partitionUpdates(orderRules)
        return correctUpdates.sumOf { it.strictMiddle() }
    }

    fun part2(input: String): Int {
        val (orderRules, updates) = input.getRulesAndUpdates()
        val (_, invalidUpdates) = updates.partitionUpdates(orderRules)

        val resorted = invalidUpdates.map { update ->
            update.sortedWith { o1, o2 ->
                when {
                    orderRules.any { it.first == o1 && it.second == o2 } -> -1
                    orderRules.any { it.second == o1 && it.first == o2 } -> 1
                    else -> 0
                }
            }
        }

        return resorted.sumOf { it.strictMiddle() }
    }

    /* Alternative
        fun List<Pair<Int, Int>>.getComparator(): Comparator<Int> {
            val byFirst = this.groupBy(Pair<Int, Int>::first, Pair<Int, Int>::second).mapValues { it.value.toSet() }
            val bySecond = this.groupBy(Pair<Int, Int>::second, Pair<Int, Int>::first).mapValues { it.value.toSet() }

            return Comparator { o1, o2 ->
                when {
                    o2 in (byFirst[o1] ?: emptySet()) -> -1
                    o1 in (bySecond[o2] ?: emptySet()) -> 1
                    else -> 0
                }
            }
        }

        fun part1(input: String): Int {
            val (orderRules, updates) = input.getRulesAndUpdates()
            val comparator = orderRules.getComparator()

            return updates.filter { update ->
                update.zipWithNext().all { comparator.compare(it.first, it.second) == -1 }
            }.sumOf {
                it.strictMiddle()
            }
        }

        fun part2(input: String): Int {
            val (orderRules, updates) = input.getRulesAndUpdates()
            val comparator = orderRules.getComparator()

            return updates.filter { update ->
                update.zipWithNext().any { comparator.compare(it.first, it.second) != -1 }
            }.map {
                it.sortedWith(comparator)
            }.sumOf {
                it.strictMiddle()
            }
        }
    */

    // Test if implementation meets criteria from the description
    val testInput = utils.readFile("test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Solve puzzle and print result
    val input = utils.readFile()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}