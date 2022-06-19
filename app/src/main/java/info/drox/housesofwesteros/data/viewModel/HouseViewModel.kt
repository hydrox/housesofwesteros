package info.drox.housesofwesteros.data.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import info.drox.housesofwesteros.data.HouseRepository
import info.drox.housesofwesteros.data.api.GoTService
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class HouseViewModel(
    application: Application,
    private val savedState: SavedStateHandle
): AndroidViewModel(application) {
    private val repository = HouseRepository(GoTService.getInstance(application))

    fun listOfHouses(): Flow<PagingData<House>> {
        return repository.getHouses().cachedIn(viewModelScope)
    }

    fun getHouse(houseId: Int): Flow<House?> {
        Log.d("HouseViewModel", "getHouse() called with: houseId = $houseId")
        return runBlocking {
            repository.getHouse(houseId)
        }
    }

    fun getCharacter(characterId: Int): Flow<Character?> {
        Log.d("HouseViewModel", "getCharacter() called with: characterId = $characterId")
        return runBlocking {
            repository.getCharacter(characterId)
        }
    }

}