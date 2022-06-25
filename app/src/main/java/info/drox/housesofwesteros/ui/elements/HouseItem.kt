package info.drox.housesofwesteros.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import info.drox.housesofwesteros.data.model.House
import info.drox.housesofwesteros.ui.theme.HousesOfWesterosTheme
import java.net.URL

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HouseItem(house: House, onClickSource: () -> Unit) {
    Card(onClick = { onClickSource() }, modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        elevation = 10.dp) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(house.name, style = MaterialTheme.typography.h5)
            Text("Region: ${house.region.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
            Text("Words: ${house.words.ifBlank { "unknown" }}", style = MaterialTheme.typography.body1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseItemPreview() {
    HousesOfWesterosTheme {
        HouseItem(House(
            URL("https://example.local/1"),
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
            URL("https://example.local/1"),
            "Test Haus",
            "Wörter sind Schall und Rauch",
            "Irgendwo", "", "", emptyArray(), emptyArray()
            , emptyArray())) {}
    }
}