package interview.case.summer21.model


interface ITextBox {
    val text: String
    val box: Rectangle
    val baseLineY: Float
}

data class TextBox(
        override val text: String,
        override val box: Rectangle,
        override val baseLineY: Float,
        val fromWordBoxes: List<WordBox>
) : ITextBox {
    companion object {

        fun fromWordBoxes(wordBoxes: List<WordBox>): TextBox = TextBox(
                text = wordBoxes.joinToString(" ") { it.text },
                box = Rectangle.union(wordBoxes.map { it.box }),
                baseLineY = wordBoxes.map { it.baseLineY }.average().toFloat(),
                fromWordBoxes = wordBoxes
        )

        fun fromTextBoxes(textBoxes: List<TextBox>): TextBox =
                fromWordBoxes(textBoxes.flatMap { it.fromWordBoxes })
    }
}

data class WordBox(
        override val text: String,
        override val box: Rectangle,
        override val baseLineY: Float
) : ITextBox

