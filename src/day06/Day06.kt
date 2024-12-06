package day06

import FacingPoint
import Point
import Utils
import x
import y

fun main() {
    val utils = Utils(6)

    fun printMap(input: List<String>, guard: FacingPoint, obstacles: Set<Point>, visitedPositions: Set<Point>) {
        for (x in input.indices) {
            val line = buildString {
                for (y in input[x].indices) {
                    val point = x to y
                    val char = when (point) {
                        in obstacles -> '#'
                        in visitedPositions -> 'X'
                        guard.position -> 'G'
                        else -> '.'
                    }
                    append(char)
                }
            }
            println(line)
        }
    }

    fun simulateGuard(input: List<String>, beforeMove: ((guard: FacingPoint) -> Boolean)? = null): Set<Point> {
        val obstacles = mutableSetOf<Point>()
        var tmpGuard: FacingPoint? = null
        for (x in input.indices) {
            for (y in input[x].indices) {
                when (input[x][y]) {
                    '#' -> obstacles.add(x to y)
                    '^' -> tmpGuard = FacingPoint(x to y, Direction.NORTH)
                    'v' -> tmpGuard = FacingPoint(x to y, Direction.SOUTH)
                    '<' -> tmpGuard = FacingPoint(x to y, Direction.WEST)
                    '>' -> tmpGuard = FacingPoint(x to y, Direction.EAST)
                }
            }
        }
        var guard = tmpGuard!!
        val visitedPositions = mutableSetOf<Point>()

        while (guard.position.x in input.indices && guard.position.y in input[0].indices) {
            visitedPositions.add(guard.position)

            if (beforeMove?.invoke(guard) == false) break

            var newGuard = guard.move()
            if (newGuard.position in obstacles) {
                newGuard = guard.turn(90)
            }
            guard = newGuard
        }

        //printMap(input, guard, obstacles, visitedPositions)

        return visitedPositions
    }

    fun getGuardPosition(input: List<String>): Point {
        val guardChars = setOf('^', 'v', '<', '>')
        return input.indexOfFirst { line -> line.any { it in guardChars } }.let { line ->
            line to input[line].indexOfFirst { it in guardChars }
        }
    }

    fun part1(input: List<String>): Int {
        val visitedPositions = simulateGuard(input)
        return visitedPositions.size
    }

    fun part2(input: List<String>): Int {
        val guardStartingPosition = getGuardPosition(input)
        val visitedPositions = simulateGuard(input).minus(guardStartingPosition)

        // Check all possible obstacle placements on the path of the guard if they create a loop.
        // Loop can be detected when a guard enters a point facing in the same direction twice.
        // Not really efficient but it gets the job done...
        return visitedPositions.count { newObstaclePosition ->
            val guardHistory = mutableSetOf<FacingPoint>()
            val modifiedMap = input.map { it.toMutableList() }.apply {
                this[newObstaclePosition.x][newObstaclePosition.y] = '#'
            }.map { it.joinToString("") }
            var loop = false
            simulateGuard(modifiedMap) { guard ->
                loop = !guardHistory.add(guard)
                return@simulateGuard !loop
            }

            return@count loop
        }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}