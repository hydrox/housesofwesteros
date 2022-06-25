package info.drox.housesofwesteros.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import info.drox.housesofwesteros.data.Converters
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import info.drox.housesofwesteros.data.model.RemoteKey

@Database(entities = [Character::class, House::class, RemoteKey::class], version = 2)
@TypeConverters(Converters::class)
abstract class GoTDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: GoTDatabase? = null

        fun getDatabase(context: Context): GoTDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoTDatabase::class.java,
                    "GoT_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun characterDao(): CharacterDao
    abstract fun houseDao(): HouseDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}