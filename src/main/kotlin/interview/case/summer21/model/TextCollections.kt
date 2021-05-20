package interview.case.summer21.model


data class TextDocument(
        val pages: List<TextPage>
)

data class TextPage(
        val width: Float,
        val height: Float,
        val words: List<WordBox>
)
