package info.drox.housesofwesteros.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import info.drox.housesofwesteros.data.model.Character

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharacterShort(character: Character, onClickSource: () -> Unit) {
    Card(onClick = { onClickSource() }) {
        Column {
            Text(character.name, style = MaterialTheme.typography.h5)
            Text("Gender: ${character.gender}", style = MaterialTheme.typography.body1)
        }
    }
}

