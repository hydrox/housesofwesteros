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
import info.drox.housesofwesteros.data.db.GoTDatabase
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.net.URL

class HouseViewModel(
    application: Application,
    private val savedState: SavedStateHandle
): AndroidViewModel(application) {
    private val repository = HouseRepository(GoTService.getInstance(application), GoTDatabase.getDatabase(application))

    fun listOfHouses(): Flow<PagingData<House>> {
        return repository.getHouses().cachedIn(viewModelScope)
    }

    fun getHouse(url: URL): Flow<House?> {
        Log.v("HouseViewModel", "getHouse() called with: url = $url")
        return runBlocking {
            repository.getHouse(url)
        }
    }

    fun getCharacter(url: URL): Flow<Character?> {
        Log.v("HouseViewModel", "getCharacter() called with: url = $url")
        return runBlocking {
            repository.getCharacter(url)
        }
    }

}