package info.drox.housesofwesteros.data.api

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.model.House
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HousesDataSource(private val api: GoTService, private val db: GoTDatabase) : PagingSource<Int, House>() {
    companion object {
        private const val TAG = "HousesDataSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, House> {
        Log.d(TAG, "load() called with: key = ${params.key}")
        return withContext(Dispatchers.IO) {
            try {
                val nextPageNumber = params.key ?: 1
                val response = api.listHouses(nextPageNumber)
                launch {
                    db.houseDao().insertAll(response)
                }

                LoadResult.Page(
                    data = response,
                    prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                    nextKey = if (response.isNotEmpty()) nextPageNumber + 1 else null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, House>): Int {
        Log.d(TAG, "getRefreshKey() called with: state = $state")
        return 1
    }
}