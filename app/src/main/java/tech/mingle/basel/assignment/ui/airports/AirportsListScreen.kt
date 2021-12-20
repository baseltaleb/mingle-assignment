package tech.mingle.basel.assignment.ui.airports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import tech.mingle.basel.assignment.R
import tech.mingle.basel.assignment.data.Constants
import tech.mingle.basel.assignment.ui.common.ScreenTitle

@Composable
fun AirportListScreen(viewModel: AirportListViewModel) {

    var pagedAirportNames by remember {
        mutableStateOf<Flow<PagingData<String>>>(emptyFlow())
    }

    val lazyAirportNames = pagedAirportNames.collectAsLazyPagingItems()

    LaunchedEffect(viewModel) {
        pagedAirportNames = viewModel.getPagedAirportsByDistance(Constants.SCHIPHOL_AIRPORT_ID)
    }
    Column {
        ScreenTitle(title = stringResource(id = R.string.airport_list_screen_title))
        when (lazyAirportNames.loadState.refresh) {
            is LoadState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            !is LoadState.Loading -> AirportListColumn(lazyAirportNames)
            is LoadState.Error -> AirportListError(lazyAirportNames.loadState.refresh)
        }
    }
}

@Composable
fun AirportListColumn(items: LazyPagingItems<String>) {

    LazyColumn {
        itemsIndexed(items) { index, airportName ->
            airportName ?: return@itemsIndexed

            Text(text = airportName, modifier = Modifier.padding(16.dp))
            Divider(thickness = 1.dp)
        }
    }
}

@Composable
fun AirportListError(state: LoadState) {
    if (state is LoadState.Error) {
        Text(text = state.error.localizedMessage ?: "Unknown error")
    }
}

@Composable
fun AirportListTopBar() {
    TopAppBar(title = { Text(text = "Airports") })
}