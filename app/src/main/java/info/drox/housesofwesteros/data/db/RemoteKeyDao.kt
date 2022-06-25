package info.drox.housesofwesteros.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import info.drox.housesofwesteros.data.model.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM RemoteKey WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): RemoteKey

    @Query("DELETE FROM RemoteKey WHERE label = :query")
    suspend fun deleteByQuery(query: String)
}