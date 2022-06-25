package info.drox.housesofwesteros.data

import androidx.room.TypeConverter
import java.net.URL

object Converters {
    @JvmStatic
    @TypeConverter
    fun urlFromString(value: String?): URL? {
        return value?.let { URL(it) }
    }

    @JvmStatic
    @TypeConverter
    fun urlToString(url: URL?): String? {
        return url?.toString()
    }

    @JvmStatic
    @TypeConverter
    fun stringArrayFromString(value: String?): Array<String>? {
        return value?.split("|||")?.toTypedArray()
    }

    @JvmStatic
    @TypeConverter
    fun stringArrayToString(array: Array<String>?): String? {
        return array?.joinToString("|||")
    }
}