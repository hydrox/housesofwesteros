package info.drox.housesofwesteros.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import info.drox.housesofwesteros.data.model.Character
import kotlinx.coroutines.flow.Flow
import java.net.URL

@Dao
interface CharacterDao {
    @Query("SELECT * FROM Character")
    fun getAll(): Flow<List<Character>>

    @Query("SELECT * FROM Character where url = :url")
    fun getByUrl(url: URL): Flow<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: List<Character>)
}