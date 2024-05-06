package dev.kr3st1k.piucompanion

import dev.kr3st1k.piucompanion.core.network.parsers.NewsListParser
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class NewsListParserUnitTest {

    private lateinit var newsListParser: NewsListParser

    @Before
    fun setup() {
        newsListParser = NewsListParser
    }

    @Test
    fun testParseEmptyHtml() {
        val document = Jsoup.parse("")

        val result = newsListParser.parse(document)

        assert(result.isEmpty())
    }

    @Test
    fun testParseSingleNewsAndCheck() {
        val htmlFile = File("src/test/java/dev/kr3st1k/piucompanion/html/news/newsone.html")
        val htmlContent = String(Files.readAllBytes(Paths.get(htmlFile.toURI())))

        val document = Jsoup.parse(htmlContent)

        val result = newsListParser.parse(document)

        assert(result.size == 1)
        val newsElement = result[0]
        assert(newsElement.name == "[PIU PHOENIX v1.08.0 update Notice]")
        assert(newsElement.link == "https://piugame.com/bbs/board.php?bo_table=phoenix_notice&wr_id=255")
        assert(newsElement.type == "Update")
    }


    @Test
    fun testParse() {
        val htmlFile = File("src/test/java/dev/kr3st1k/piucompanion/html/news/newslist.html")
        val htmlContent = String(Files.readAllBytes(Paths.get(htmlFile.toURI())))

        val document = Jsoup.parse(htmlContent)

        val result = newsListParser.parse(document)

        assert(result.isNotEmpty())
    }
}