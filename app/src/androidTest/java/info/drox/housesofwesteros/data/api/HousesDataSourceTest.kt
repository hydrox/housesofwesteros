package info.drox.housesofwesteros.data.api

import android.util.Log
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.mockGoTService
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
            URL("https://example.local/1"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()),
        House(
            URL("https://example.local/2"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()),
        House(
            URL("https://example.local/3"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()),
        House(
            URL("https://example.local/4"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray())
    )
    private val mockApi = mockGoTService(mockHouses, emptyList())

    private val mockDb = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        GoTDatabase::class.java
    )
    .fallbackToDestructiveMigration()
    .build()

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoad() = runTest {
        val remoteMediator = HousesDataSource(api = mockApi, db = mockDb, 2)
        Log.d("iae", mockApi.listHouses(1, 2).toString())
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
        Log.d("iae", mockApi.listHouses(1, 5).toString())
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
        // Set up failure message to throw exception from the mock API.
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
        // Clear out failure message to default to the successful response.
    }
}