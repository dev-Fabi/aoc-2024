typealias Point = Pair<Int, Int>

val Point.x: Int get() = first
val Point.y: Int get() = second

data class FacingPoint(val position: Point, val direction: Direction) {
    fun move(delta: Int = 1): FacingPoint {
        return when (direction) {
            Direction.NORTH -> copy(position = position.copy(first = position.x - delta))
            Direction.EAST -> copy(position = position.copy(second = position.y + delta))
            Direction.SOUTH -> copy(position = position.copy(first = position.x + delta))
            Direction.WEST -> copy(position = position.copy(second = position.y - delta))
        }
    }

    /** @param degrees how many degrees to turn to the right. Must be divisible by 90 and can be negative. */
    fun turn(degrees: Int): FacingPoint {
        require(degrees % 90 == 0) { "degress must be divisible by 90" }
        val newDirectionOrdinal = (direction.ordinal + (degrees / 90)) % Direction.entries.size
        return copy(direction = Direction.entries[newDirectionOrdinal])
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}