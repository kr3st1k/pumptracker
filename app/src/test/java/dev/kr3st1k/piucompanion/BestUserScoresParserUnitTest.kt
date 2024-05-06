package dev.kr3st1k.piucompanion

import dev.kr3st1k.piucompanion.core.modules.BgManager
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.network.parsers.BestUserScoresParser
import org.jsoup.Jsoup
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class BestUserScoresParserUnitTest {

    private lateinit var bestUserScoresParser: BestUserScoresParser

    companion object {
        @Mock
        private lateinit var bgManager: BgManager

        private val bgs = mutableListOf(
            BgInfo("jacket_url", "song_name")
        )

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            stopKoin()
            startKoin {
                modules(module {
                    single { bgManager }
                })
            }
        }

        @JvmStatic
        @AfterClass
        fun teardownClass() {
            stopKoin()
        }
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(bgManager.readBgJson()).thenReturn(bgs)
        bestUserScoresParser = BestUserScoresParser
    }

    @Test
    fun testParseEmptyHtml() {
        val document = Jsoup.parse("")

        val result = bestUserScoresParser.parse(document)

        assert(result.res.isEmpty())
    }

    @Test
    fun testParseSingleScoreAndCheck() {
        val htmlFile = File("src/test/java/dev/kr3st1k/piucompanion/html/best/singlebestscore.html")
        val htmlContent = String(Files.readAllBytes(Paths.get(htmlFile.toURI())))

        val document = Jsoup.parse(htmlContent)

        val result = bestUserScoresParser.parse(document)

        assert(result.res.size == 1)
        val bestUserScore = result.res[0]
        assert(bestUserScore.songName == "song_name")
        assert(bestUserScore.rank == "AAA")
        assert(bestUserScore.score == "1,000,000")
    }


    @Test
    fun testParse() {
        val htmlFile = File("src/test/java/dev/kr3st1k/piucompanion/html/best/bestscores.html")
        val htmlContent = String(Files.readAllBytes(Paths.get(htmlFile.toURI())))

        val document = Jsoup.parse(htmlContent)

        val result = bestUserScoresParser.parse(document)

        assert(result.res.isNotEmpty())
    }
}