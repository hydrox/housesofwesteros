@file:OptIn(ExperimentalMaterialApi::class)

package info.drox.housesofwesteros

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import info.drox.housesofwesteros.data.api.GoTService
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.model.House
import info.drox.housesofwesteros.data.viewModel.HouseViewModel
import info.drox.housesofwesteros.ui.theme.HousesOfWesterosTheme
import java.net.URL

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
                                val houseState = remember { viewModel.getHouse(it) }.collectAsState(initial = null)
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
                                val characterState = remember { viewModel.getCharacter(it) }.collectAsState(initial = null)
                                characterState.value?.let { character ->
                                    CharacterDetails(character = character, navHostController = navController)
                                }
                            }
                        }
                    }
                }
                // A surface container using the 'background' color from the theme
            }
        }
        //Log.d("MainActivity", GoTService().listHouses(1).toString())
    }
}

@Composable
fun HousesList(navHostController: NavHostController, viewModel: HouseViewModel = viewModel()) {

    val housesList = remember { viewModel.listOfHouses() }.collectAsLazyPagingItems()
    /* ... */
    LazyColumn {
        items(housesList) { house ->
            house?.let {
                HouseItem(house) { navHostController.navigate("houses/${Uri.parse(house.url.toString()).lastPathSegment}") }
            }
        }
    }
}

@Composable
fun HouseItem(house: House, onClickSource: () -> Unit) {
    Card(onClick = { onClickSource() }, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Column {
            Text(house.name, style = MaterialTheme.typography.h5)
            Text("Region: ${house.region.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
            Text("Words: ${house.words.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun HouseDetails(house: House, navHostController: NavHostController? = null, viewModel: HouseViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(house.name, style = MaterialTheme.typography.h3)
        Text("Region: ${house.region.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
        Text("Words: ${house.words.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
        Text("Coat of Arms: ${house.coatOfArms.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
        if (house.titles.any { it != "" }) {
            Text("Titles", style = MaterialTheme.typography.h4)
            for (title in house.titles) {
                Text(title, modifier = Modifier.padding(start = 12.dp), style = MaterialTheme.typography.body1)
            }
        }
        if (house.swornMembers.isNotEmpty()) {
            Text("Members", style = MaterialTheme.typography.h4)
            for (member in house.swornMembers) {
                Uri.parse(member).lastPathSegment?.let { characterId ->
                    val characterState =
                        remember { viewModel.getCharacter(characterId.toInt()) }.collectAsState(
                            initial = null
                        )
                    characterState.value?.let { character ->
                        CharacterShort(character) { navHostController?.navigate("characters/${characterId}") }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterShort(character: Character, onClickSource: () -> Unit) {
    Card(onClick = { onClickSource() }) {
        Column {
            Text(character.name, style = MaterialTheme.typography.h5)
            Text("Gender: ${character.gender}", style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun CharacterDetails(character: Character, navHostController: NavHostController, viewModel: HouseViewModel = viewModel()) {
    Column {
        Text("Name: ${character.name}", style = MaterialTheme.typography.h3)
        if (character.born != "") {
            Text("Birth: ${character.born}", style = MaterialTheme.typography.body1)
        }
        if (character.died != "") {
            Text("Death: ${character.died}", style = MaterialTheme.typography.body1)
        }
        if (character.gender != "") {
            Text("Gender: ${character.gender}", style = MaterialTheme.typography.body1)
        }
        if (character.culture != "") {
            Text("Culture: ${character.culture}", style = MaterialTheme.typography.body1)
        }
        character.mother.let {
            Uri.parse(it).lastPathSegment?.let { characterId ->
                val characterState =
                    remember { viewModel.getCharacter(characterId.toInt()) }.collectAsState(
                        initial = null
                    )
                characterState.value?.let { character ->
                    Row {
                        Text("Mother", style = MaterialTheme.typography.h4)
                        CharacterShort(character) { navHostController.navigate("characters/${characterId}") }
                    }
                }
            }
        }
        character.father.let {
            Uri.parse(it).lastPathSegment?.let { characterId ->
                val characterState =
                    remember { viewModel.getCharacter(characterId.toInt()) }.collectAsState(
                        initial = null
                    )
                characterState.value?.let { character ->
                    Row {
                        Text("Father", style = MaterialTheme.typography.h4)
                        CharacterShort(character) { navHostController.navigate("characters/${characterId}") }
                    }
                }
            }
        }
        character.spouse.let {
            Uri.parse(it).lastPathSegment?.let { characterId ->
                Text("Spouse", style = MaterialTheme.typography.h4)
                val characterState =
                    remember { viewModel.getCharacter(characterId.toInt()) }.collectAsState(
                        initial = null
                    )
                characterState.value?.let { character ->
                    CharacterShort(character) { navHostController.navigate("characters/${characterId}") }
                }
            }
        }
        if (character.titles.any { it != "" }) {
            Text("Titles", style = MaterialTheme.typography.h4)
            for (title in character.titles) {
                Text(title, modifier = Modifier.padding(start = 12.dp), style = MaterialTheme.typography.body1)
            }
        }
        if (character.aliases.any { it != "" }) {
            Text("Aliases", style = MaterialTheme.typography.h4)
            for (title in character.titles) {
                Text(title, modifier = Modifier.padding(start = 12.dp), style = MaterialTheme.typography.body1)
            }
        }

        if (character.allegiances.any { it != "" }) {
            Text("Allegiances", style = MaterialTheme.typography.h4)
            for (allegiance in character.allegiances) {
                Uri.parse(allegiance).lastPathSegment?.let { houseId ->
                    val houseState =
                        remember { viewModel.getHouse(houseId.toInt()) }.collectAsState(
                            initial = null
                        )
                    houseState.value?.let { house ->
                        HouseItem(house) { navHostController.navigate("houses/${houseId}") }
                    }
                }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HouseItemPreview() {
    HousesOfWesterosTheme {
        HouseItem(House(
            URL("https://example.local"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray())) {}
    }
}

@Preview(showBackground = true)
@Composable
fun HouseItemDarkPreview() {
    HousesOfWesterosTheme(darkTheme = true) {
        HouseItem(House(
            URL("https://example.local"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray())) {}
    }
}
@Preview(showBackground = true)
@Composable
fun HouseDetailsPreview() {
    HousesOfWesterosTheme {
        HouseDetails(House(
            URL("https://example.local"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()), navHostController = null)
    }
}

@Preview(showBackground = true)
@Composable
fun HouseDetailsDarkPreview() {
    HousesOfWesterosTheme(darkTheme = true) {
        HouseDetails(House(
            URL("https://example.local"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray()), navHostController = null)
    }
}
