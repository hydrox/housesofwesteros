package info.drox.housesofwesteros.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(@PrimaryKey val label: String, val nextKey: Int?)