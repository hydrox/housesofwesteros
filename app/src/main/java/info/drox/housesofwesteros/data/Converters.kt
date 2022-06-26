package info.drox.housesofwesteros.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
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
    fun stringArrayfromString(value: String?): Array<String> {
        val listType: Type = object : TypeToken<Array<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @JvmStatic
    @TypeConverter
    fun stringArrayToString(array: Array<String?>?): String {
        val gson = Gson()
        return gson.toJson(array)
    }
}