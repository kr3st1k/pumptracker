package dev.kr3st1k.piucompanion

import dev.kr3st1k.piucompanion.core.network.parsers.LatestScoresParser
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class LatestScoresParserUnitTest {

    private lateinit var latestScoresParser: LatestScoresParser

    @Before
    fun setup() {
        latestScoresParser = LatestScoresParser
    }

    @Test
    fun testParseEmptyHtml() {
        val document = Jsoup.parse("")

        val result = latestScoresParser.parse(document)

        assert(result.isEmpty())
    }

    @Test
    fun testParseSingleScoreAndCheck() {
        val htmlFile =
            File("src/test/java/dev/kr3st1k/piucompanion/html/latest/latestonescore.html")
        val htmlContent = String(Files.readAllBytes(Paths.get(htmlFile.toURI())))

        val document = Jsoup.parse(htmlContent)

        val result = latestScoresParser.parse(document)

        assert(result.size == 1)
        val userScore = result[0]
        assert(userScore.songName == "Showdown")
        assert(userScore.rank == "F")
        assert(userScore.score == "STAGE BREAK")
    }


    @Test
    fun testParse() {
        val htmlFile = File("src/test/java/dev/kr3st1k/piucompanion/html/latest/latest.html")
        val htmlContent = String(Files.readAllBytes(Paths.get(htmlFile.toURI())))

        val document = Jsoup.parse(htmlContent)

        val result = latestScoresParser.parse(document)

        assert(result.isNotEmpty())
    }
}