package info.drox.housesofwesteros.data.api

import android.content.Context
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GoTService {
    companion object {
        const val BASE_URL = "https://www.anapioficeandfire.com/api/"

        fun getInstance(context: Context): GoTService {
            val cacheSize: Long = 10 * 1024 * 1024 // 10 MB

            val cache = Cache(context.cacheDir, cacheSize)
            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GoTService::class.java)
        }
    }

    @GET("houses")
    suspend fun listHouses(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 10
    ): List<House>

    @GET("houses/{houseId}")
    suspend fun getHouse(@Path("houseId") houseId: Int): House

    @GET("characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): Character
}