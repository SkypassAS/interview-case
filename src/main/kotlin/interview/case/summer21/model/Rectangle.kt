package interview.case.summer21.model

data class Rectangle(
        val leftX: Float,
        val topY: Float,
        val rightX: Float,
        val bottomY: Float
) {
    val width: Float
            get() = rightX - leftX
    val height: Float
            get() = bottomY - topY

    init {
        if (width < 0f) {
            throw IllegalArgumentException("negative width: $this")
        }
        if (height < 0f) {
            throw IllegalArgumentException("negative height: $this")
        }
    }

    companion object {
        fun union(rectangles: List<Rectangle>): Rectangle {
            if (rectangles.isEmpty()) throw IllegalArgumentException("Cannot create union from no rectangles")
            return Rectangle(
                    leftX = rectangles.map { it.leftX }.min()!!,
                    rightX = rectangles.map { it.rightX }.max()!!,
                    topY = rectangles.map { it.topY }.min()!!,
                    bottomY = rectangles.map { it.bottomY }.max()!!
            )
        }
    }

    fun intersects(other: Rectangle): Boolean =
            leftX < other.rightX && rightX > other.leftX &&
                    topY < other.bottomY && bottomY > other.topY

    fun contains(other: Rectangle): Boolean =
            leftX <= other.leftX && rightX >= other.rightX &&
                    topY <= other.topY && bottomY >= other.bottomY

    fun createBelow(height: Float): Rectangle = copy(
            topY = bottomY,
            bottomY = bottomY + height
    )

    fun growLeft(value: Float): Rectangle =
            if (value > 0f) copy(leftX = leftX - value)
            else throw IllegalArgumentException("Value must be positive")

    fun growRight(value: Float): Rectangle =
            if (value > 0f) copy(rightX = rightX + value)
            else throw IllegalArgumentException("Value must be positive")

    fun shrinkLeft(value: Float): Rectangle {
        if (value < 0f) throw IllegalArgumentException("Value must be positive")
        val newLeftX = leftX + value
        return copy(leftX = if (newLeftX <= rightX) newLeftX else rightX)
    }

    fun shrinkRight(value: Float): Rectangle {
        if (value < 0f) throw IllegalArgumentException("Value must be positive")
        val newRightX = rightX - value
        return copy(rightX = if (newRightX >= leftX) newRightX else leftX)
    }
}