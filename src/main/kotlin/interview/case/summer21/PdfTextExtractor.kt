package interview.case.summer21

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.*
import com.itextpdf.text.Rectangle as ItextRectangle
import interview.case.summer21.model.TextDocument
import interview.case.summer21.model.TextPage
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.*
import interview.case.summer21.model.Rectangle
import interview.case.summer21.model.WordBox


class PdfTextExtractor {

    fun extractDocument(pdfData: ByteArray): TextDocument {
        val reader = PdfReader(pdfData)
        val contentParser = PdfReaderContentParser(reader)
        val textPages: List<TextPage> = (1..reader.numberOfPages)
                .map { pageNumber ->
                    val pageSize: ItextRectangle = reader.getPageSize(pageNumber)
                    val locationStrategy = WordBoxLocationStrategy(pageSize)
                    contentParser.processContent(pageNumber, LocationTextExtractionStrategy(locationStrategy))
                    val wordBoxes = locationStrategy.wordBoxes
                    TextPage(
                            words = wordBoxes,
                            width = pageSize.width,
                            height = pageSize.height
                    )
                }
        return TextDocument(pages = textPages)
    }
}


class WordBoxLocationStrategy(
        private val pageSize: ItextRectangle
) : TextChunkLocationStrategy {
    private val _wordBoxes: MutableList<WordBox> = mutableListOf()
    val wordBoxes: List<WordBox>
        get() = _wordBoxes.toList()

    override fun createLocation(renderInfo: TextRenderInfo, baseline: LineSegment): TextChunkLocation {
        val wordsCharInfo: List<List<TextRenderInfo>> = renderInfo.characterRenderInfos
                .splitOn { charInfo -> charInfo.text == " " }
        renderInfo.mcid
        val wordBoxes = wordsCharInfo
                .map { wordCharInfo ->
                    val firstBaseline = wordCharInfo.first().baseline
                    val lastBaseline = wordCharInfo.last().baseline
                    val text: String = wordCharInfo.joinToString("") { it.text }
                    val leftX: Float = firstBaseline.startPoint[Vector.I1]
                    val rightX: Float = lastBaseline.endPoint[Vector.I1]
                    val topY: Float = wordCharInfo.first().ascentLine.startPoint[Vector.I2]
                    val bottomY: Float = wordCharInfo.first().descentLine.startPoint[Vector.I2]
                    val baselineY: Float = wordCharInfo.map { it.baseline.startPoint[Vector.I2] }.average().toFloat()
                    WordBox(
                            text = text,
                            box = Rectangle(
                                    leftX = leftX,
                                    rightX = rightX,
                                    topY = pageSize.height - topY,
                                    bottomY = pageSize.height - bottomY
                            ),
                            baseLineY = pageSize.height - baselineY
                    )
                }
        _wordBoxes.addAll(wordBoxes)
        return TextChunkLocationDefaultImp(baseline.startPoint, baseline.endPoint, renderInfo.singleSpaceWidth)
    }
}