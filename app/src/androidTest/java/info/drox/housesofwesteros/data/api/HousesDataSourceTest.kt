package info.drox.housesofwesteros.data.api

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.MockGoTService
import info.drox.housesofwesteros.data.model.House
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HousesDataSourceTest {
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
    private val mockApi = MockGoTService(mockHouses, emptyList())

    private val mockDb = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        GoTDatabase::class.java
    )
    .fallbackToDestructiveMigration()
    .build()

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoad() = runTest {
        val remoteMediator = HousesDataSource(api = mockApi, db = mockDb, 2)
        val pagingState = PagingState<Int, House>(
            listOf(),
            null,
            PagingConfig(2),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun loadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val remoteMediator = HousesDataSource(api = mockApi, db = mockDb, 5)
        val pagingState = PagingState<Int, House>(
            listOf(),
            null,
            PagingConfig(5),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        val remoteMediator = HousesDataSource(api = mockApi, db = mockDb, 2)
        mockApi.fails = true

        val pagingState = PagingState<Int, House>(
            listOf(),
            null,
            PagingConfig(2),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
        mockApi.fails = false
    }
}