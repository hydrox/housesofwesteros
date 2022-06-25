package info.drox.housesofwesteros.data.api

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.model.House
import info.drox.housesofwesteros.data.model.RemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class HousesDataSource(private val api: GoTService, private val db: GoTDatabase, private val pagingSize: Int) : RemoteMediator<Int, House>() {
    companion object {
        private const val TAG = "HousesDataSource"
    }
    private val houseDao = db.houseDao()
    private val remoteKeyDao = db.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, House>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKeyByQuery("houses")
                    }

                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    remoteKey.nextKey
                }
            }
            val page = loadKey ?: 1
            val response = api.listHouses(page = page, pageSize = pagingSize)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByQuery("houses")
                    houseDao.clear()
                }

                remoteKeyDao.insertOrReplace(RemoteKey("houses", page + 1))
                db.houseDao().insertAll(response)
            }
            MediatorResult.Success(endOfPaginationReached = response.size != pagingSize)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}