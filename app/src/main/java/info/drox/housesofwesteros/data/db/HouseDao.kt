package info.drox.housesofwesteros.data.db

import androidx.paging.PagingSource
import androidx.room.*
import info.drox.housesofwesteros.data.model.House
import kotlinx.coroutines.flow.Flow
import java.net.URL

@Dao
interface HouseDao {
    @Query("SELECT * FROM House")
    fun getAll(): Flow<List<House>>

    @Query("SELECT * FROM House")
    fun getPagingSource(): PagingSource<Int,House>

    @Query("SELECT * FROM House where url = :url")
    fun getByUrl(url: URL): Flow<House>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(house: House)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(houses: List<House>)

    @Query("DELETE FROM House")
    fun clear()
}