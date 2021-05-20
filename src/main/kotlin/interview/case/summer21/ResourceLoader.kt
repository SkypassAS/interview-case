package interview.case.summer21

import java.net.URL

object ResourceLoader {
    fun getAllSubPathsInResourceDirectory(dirPath: String): List<String> {
        val dirUrl: URL = ResourceLoader::class.java.getResource(dirPath)
        return dirUrl.openStream().bufferedReader().readLines()
                .map { "$dirPath/$it" }
    }

    fun loadPdfData(resourcePath: String): ByteArray? {
        val resource = ResourceLoader::class.java.getResource(resourcePath)
        return resource?.readBytes()
    }
}