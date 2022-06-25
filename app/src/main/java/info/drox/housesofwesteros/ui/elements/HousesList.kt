package info.drox.housesofwesteros.ui.elements

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import info.drox.housesofwesteros.data.viewModel.HouseViewModel

@Composable
fun HousesList(navHostController: NavHostController, viewModel: HouseViewModel = viewModel()) {
    val housesList = remember { viewModel.listOfHouses() }.collectAsLazyPagingItems()
    LazyColumn {
        items(housesList) { house ->
            house?.let {
                HouseItem(house) { navHostController.navigate("houses/${house.id}") }
            }
        }
    }
}