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

    fun part1(input: List<String>): Int {
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
            var newGuard = guard.move()
            if (newGuard.position in obstacles) {
                newGuard = guard.turn(90)
            }
            guard = newGuard
        }

        //printMap(input, guard, obstacles, visitedPositions)

        return visitedPositions.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 41)
    //check(part2(testInput) == 6)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}