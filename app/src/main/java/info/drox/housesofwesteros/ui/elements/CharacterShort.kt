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
import androidx.compose.ui.unit.dp
import info.drox.housesofwesteros.data.model.Character

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharacterShort(character: Character, onClickSource: () -> Unit) {
    Card(onClick = { onClickSource() }, modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        elevation = 10.dp) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(character.name.ifBlank { "Name unknown" }, style = MaterialTheme.typography.h5)
            Text("Gender: ${character.gender}", style = MaterialTheme.typography.body1)
        }
    }
}

