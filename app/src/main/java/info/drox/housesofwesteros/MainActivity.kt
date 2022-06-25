package info.drox.housesofwesteros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import info.drox.housesofwesteros.data.api.GoTService
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import info.drox.housesofwesteros.data.viewModel.HouseViewModel
import info.drox.housesofwesteros.ui.elements.CharacterDetails
import info.drox.housesofwesteros.ui.elements.HouseDetails
import info.drox.housesofwesteros.ui.elements.HousesList
import info.drox.housesofwesteros.ui.theme.HousesOfWesterosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val uri = GoTService.BASE_URL

            HousesOfWesterosTheme {
                NavHost(navController = navController, startDestination = "houses") {
                    composable("houses", deepLinks = listOf(navDeepLink { uriPattern = "$uri/houses" })) {
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                            Column {
                                Text("Houses of Westeros", modifier = Modifier, style = MaterialTheme.typography.h2)
                                HousesList(navController)
                            }
                        }
                    }
                    composable("houses/{houseId}",
                        arguments = listOf(navArgument("houseId") {
                            type = NavType.IntType
                        }) , deepLinks = listOf(navDeepLink { uriPattern = "$uri/houses/{houseId}" })) { backStackEntry ->
                        val viewModel: HouseViewModel = viewModel()
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                            backStackEntry.arguments?.getInt("houseId")?.let {
                                val houseState = remember { viewModel.getHouse(House.getUrlFromId(it)) }.collectAsState(initial = null)
                                houseState.value?.let { house ->
                                    HouseDetails(house = house, navHostController = navController)
                                }
                            }
                        }
                    }
                    composable("characters/{characterId}",
                        arguments = listOf(navArgument("characterId") {
                            type = NavType.IntType
                        }) , deepLinks = listOf(navDeepLink { uriPattern = "$uri/characters/{characterId}" })) { backStackEntry ->
                        val viewModel: HouseViewModel = viewModel()
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                            backStackEntry.arguments?.getInt("characterId")?.let {
                                val characterState = remember { viewModel.getCharacter(Character.getUrlFromId(it)) }.collectAsState(initial = null)
                                characterState.value?.let { character ->
                                    CharacterDetails(character = character, navHostController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
        //Log.d("MainActivity", GoTService().listHouses(1).toString())
    }
}
