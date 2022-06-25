package info.drox.housesofwesteros.ui.elements

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import info.drox.housesofwesteros.data.model.House
import info.drox.housesofwesteros.data.viewModel.HouseViewModel
import info.drox.housesofwesteros.ui.theme.HousesOfWesterosTheme
import java.net.URL

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
                val characterState =
                    remember { viewModel.getCharacter(URL(member)) }.collectAsState(
                        initial = null
                    )
                characterState.value?.let { character ->
                    CharacterShort(character) { navHostController?.navigate("characters/${character.id}") }
                }
            }
        }
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