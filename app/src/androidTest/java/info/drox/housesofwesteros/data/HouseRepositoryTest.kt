package info.drox.housesofwesteros.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HouseRepositoryTest {
    private val mockHouses = listOf(
        House(
            URL("https://www.anapioficeandfire.com/api/houses/1"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()),
        House(
            URL("https://www.anapioficeandfire.com/api/houses/2"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()),
        House(
            URL("https://www.anapioficeandfire.com/api/houses/3"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()),
        House(
            URL("https://www.anapioficeandfire.com/api/houses/4"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray())
    )

    private val mockCharacter = listOf(
        Character(
            URL("https://www.anapioficeandfire.com/api/characters/1"),
            "Test",
            "Male",
            "Irgendwo", "", "", "", "", "", emptyArray(), emptyArray()
            , emptyArray()
        ),
        Character(
            URL("https://www.anapioficeandfire.com/api/characters/2"),
            "Test",
            "Male",
            "Irgendwo", "", "", "", "", "", emptyArray(), emptyArray()
            , emptyArray()
        ),
        Character(
            URL("https://www.anapioficeandfire.com/api/characters/3"),
            "Test",
            "Male",
            "Irgendwo", "", "", "", "", "", emptyArray(), emptyArray()
            , emptyArray()
        ),
        Character(
            URL("https://www.anapioficeandfire.com/api/characters/4"),
            "Test",
            "Male",
            "Irgendwo", "", "", "", "", "", emptyArray(), emptyArray()
            , emptyArray()
        ),
    )
    private val mockApi = MockGoTService(mockHouses, mockCharacter)

    private val mockDb = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        GoTDatabase::class.java
    )
        .fallbackToDestructiveMigration()
        .build()

    @Test
    fun loadCharacter() = runTest {
        val repository = HouseRepository(mockApi, mockDb)
        val flow = repository.getCharacter(Character.getUrlFromId(1))
        val character = flow.first()
        assertNotNull(character)

        val mockCharacter = mockApi.getCharacter(1)
        assertEquals(mockCharacter, character)
    }

    @Test(expected = HttpException::class)
    fun failToLoadNonExistingCharacter() = runTest {
        val repository = HouseRepository(mockApi, mockDb)

        val firstItem = repository.getCharacter(Character.getUrlFromId(-1)).first() // Returns the first item in the flow
        assertNull(firstItem)
    }

    @Test
    fun loadHouse() = runTest {
        val repository = HouseRepository(mockApi, mockDb)
        val flow = repository.getHouse(House.getUrlFromId(1))
        val house = flow.first()
        assertNotNull(house)

        val mockHouse = mockApi.getHouse(1)
        assertEquals(mockHouse, house)
    }

    @Test
    fun failToLoadNonExistingHouse() = runTest {
        val repository = HouseRepository(mockApi, mockDb)

        val firstItem = repository.getHouse(House.getUrlFromId(-1)).first() // Returns the first item in the flow
        assertNull(firstItem)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
        mockApi.fails = false
    }
}