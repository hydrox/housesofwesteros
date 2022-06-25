package info.drox.housesofwesteros.data

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import info.drox.housesofwesteros.data.api.GoTService
import info.drox.housesofwesteros.data.api.HousesDataSource
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalPagingApi::class)
class HouseRepository(application: Application, private val api: GoTService, private val pageSize: Int = 10) {
    private val db = GoTDatabase.getDatabase(application)

    fun getHouses(): Flow<PagingData<House>> {
        return Pager(config = PagingConfig(pageSize = pageSize, prefetchDistance = 50), remoteMediator = HousesDataSource(api, db, pageSize)) {
            db.houseDao().getPagingSource()
        }.flow
    }

    suspend fun getHouse(url: URL): Flow<House?> = withContext(Dispatchers.IO) {
        CoroutineScope(Dispatchers.IO).launch {
            db.houseDao().insert(api.getHouse(House.getIDFromUrl(url)))
        }
        db.houseDao().getByUrl(url)
    }

    suspend fun getCharacter(url: URL): Flow<Character?> = withContext(Dispatchers.IO) {
        CoroutineScope(Dispatchers.IO).launch {
            db.characterDao().insert(api.getCharacter(Character.getIDFromUrl(url)))
        }
        db.characterDao().getByUrl(url)
    }
}