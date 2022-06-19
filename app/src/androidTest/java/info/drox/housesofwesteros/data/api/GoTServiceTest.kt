package info.drox.housesofwesteros.data.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoTServiceTest {
    private lateinit var apiHelper: GoTService

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        apiHelper = GoTService.getInstance(appContext)
    }

    @Test
    fun fetch_character_206_success(){
        val  actualResponse = runBlocking { apiHelper.getCharacter(206) }

        assertNotNull(actualResponse)
        assertEquals("Brandon Stark", actualResponse.name)
        assertEquals("Male", actualResponse.gender)
    }

    @Test(expected = retrofit2.HttpException::class)
    fun fetch_character_negative1_failure(){
        runBlocking { apiHelper.getCharacter(-1) }
    }

    @Test
    fun fetch_house_362_success(){
        val  actualResponse = runBlocking { apiHelper.getHouse(362) }

        assertNotNull(actualResponse)
        assertEquals("House Stark of Winterfell", actualResponse.name)
        assertEquals("The North", actualResponse.region)
    }

    @Test(expected = retrofit2.HttpException::class)
    fun fetch_house_negative1_failure(){
        runBlocking { apiHelper.getHouse(-1) }
    }
}