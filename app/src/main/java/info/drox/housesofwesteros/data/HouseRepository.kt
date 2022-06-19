package info.drox.housesofwesteros.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import info.drox.housesofwesteros.data.api.GoTService
import info.drox.housesofwesteros.data.api.HousesDataSource
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HouseRepository(private val api: GoTService) {
    fun getHouses(): Flow<PagingData<House>> {
        return Pager(config = PagingConfig(pageSize = 20, prefetchDistance = 50), pagingSourceFactory = {
                HousesDataSource(api)
            }).flow
    }

    suspend fun getHouse(houseId: Int): Flow<House?> = withContext(Dispatchers.IO) {
        val resultFlow = MutableSharedFlow<House?>()
        CoroutineScope(Dispatchers.Main).launch {
            resultFlow.emit(api.getHouse(houseId))
        }
        resultFlow
    }

    suspend fun getCharacter(houseId: Int): Flow<Character?> = withContext(Dispatchers.IO) {
        val resultFlow = MutableSharedFlow<Character?>()
        CoroutineScope(Dispatchers.Main).launch {
            resultFlow.emit(api.getCharacter(houseId))
        }
        resultFlow
    }
}