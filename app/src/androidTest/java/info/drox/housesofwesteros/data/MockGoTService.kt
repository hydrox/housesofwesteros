package info.drox.housesofwesteros.data

import info.drox.housesofwesteros.data.api.GoTService
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Integer.min

class mockGoTService(
    private val mockHouses: List<House>,
    private val mockCharacters: List<Character>,
) : GoTService {
    var fails: Boolean = false
    override suspend fun listHouses(page: Int, pageSize: Int): List<House> {
        if (fails) throw HttpException(Response.error<List<House>>(404, ResponseBody.create(null, "")))
        if (pageSize*(page-1) > mockHouses.size) return emptyList()
        return mockHouses.subList((page-1)*pageSize, min((page-1)*pageSize + pageSize, mockHouses.size))
    }

    override suspend fun getHouse(houseId: Int): House {
        if (fails || houseId < 1 || mockHouses.size > houseId) throw HttpException(Response.error<List<House>>(404, ResponseBody.create(null, "")))
        return mockHouses[houseId - 1]
    }

    override suspend fun getCharacter(characterId: Int): Character {
        if (fails || characterId < 1 || mockHouses.size > characterId) throw HttpException(Response.error<List<Character>>(404, ResponseBody.create(null, "")))
        return mockCharacters[characterId - 1]
    }
}