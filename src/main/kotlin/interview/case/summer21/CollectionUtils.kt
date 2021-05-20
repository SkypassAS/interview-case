package interview.case.summer21

import com.itextpdf.text.pdf.parser.TextRenderInfo


fun <E> List<E>.splitOn(predicate: (elem: E) -> Boolean): List<List<E>> {
    val result: MutableList<MutableList<E>> = mutableListOf(mutableListOf())
    forEach { elem ->
        if (predicate(elem)) {
            result.add(mutableListOf())
        } else {
            result.last().add(elem)
        }
    }
    val resultWithoutEmpty = result
            .filter { it.isNotEmpty() }
    return resultWithoutEmpty
}

fun <E> List<E>.splitAfter(predicate: (elem: E) -> Boolean): List<List<E>> {
    return splitAfter() { elem, _, _ -> predicate(elem)}
}
fun <E> List<E>.splitAfter(predicate: (elem: E, prevElement: E?) -> Boolean): List<List<E>> {
    return splitAfter() { elem, prevElem, _ -> predicate(elem, prevElem)}
}
fun <E> List<E>.splitAfter(predicate: (elem: E, prevElement: E?, nextElement: E?) -> Boolean): List<List<E>> {
    val result: MutableList<MutableList<E>> = mutableListOf(mutableListOf())
    indices.forEach { i ->
        val elem = get(i)
        result.last().add(elem)
        if (predicate(elem, getOrNull(i-1), getOrNull(i+1))) {
            result.add(mutableListOf())
        }
    }
    val resultWithoutEmpty = result.filter { it.isNotEmpty() }
    return resultWithoutEmpty
}

fun <E> List<E>.splitBetween(predicate: (firstElem: E, secondElem: E) -> Boolean): List<List<E>> {
    if (size < 2) {
        return listOf(this)
    }
    val result: MutableList<MutableList<E>> = mutableListOf(mutableListOf(this.first()))

    indices.drop(1).forEach { i ->
        val firstElem = get(i-1)
        val secondElem = get(i)
        if (predicate(firstElem, secondElem)) {
            result.add(mutableListOf())
        }
        result.last().add(secondElem)
    }
    return result
}