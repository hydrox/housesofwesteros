package info.drox.housesofwesteros.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import info.drox.housesofwesteros.data.model.Character
import info.drox.housesofwesteros.data.viewModel.HouseViewModel
import java.net.URL

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
            if (it == "") {
                return@let
            }
            val characterState =
                remember { viewModel.getCharacter(URL(it)) }.collectAsState(
                    initial = null
                )
            characterState.value?.let { character ->
                Row {
                    Text("Mother", style = MaterialTheme.typography.h4)
                    CharacterShort(character) { navHostController.navigate("characters/${character.id}") }
                }
            }
        }
        character.father.let {
            if (it == "") {
                return@let
            }
            val characterState =
                remember { viewModel.getCharacter(URL(it)) }.collectAsState(
                    initial = null
                )
            characterState.value?.let { character ->
                Row {
                    Text("Father", style = MaterialTheme.typography.h4)
                    CharacterShort(character) { navHostController.navigate("characters/${character.id}") }
                }
            }
        }
        character.spouse.let {
            if (it == "") {
                return@let
            }
            Text("Spouse", style = MaterialTheme.typography.h4)
            val characterState =
                remember { viewModel.getCharacter(URL(it)) }.collectAsState(
                    initial = null
                )
            characterState.value?.let { character ->
                CharacterShort(character) { navHostController.navigate("characters/${character.id}") }
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
                val houseState =
                    remember { viewModel.getHouse(URL(allegiance)) }.collectAsState(
                        initial = null
                    )
                houseState.value?.let { house ->
                    HouseItem(house) { navHostController.navigate("houses/${house.id}") }
                }
            }
        }

    }
}