package tech.mingle.basel.assignment.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.mingle.basel.assignment.data.Constants
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.MapCameraState
import tech.mingle.basel.assignment.data.models.Resource
import tech.mingle.basel.assignment.ui.common.AssignmentAlertDialog
import tech.mingle.basel.assignment.ui.theme.BottomSheetShape
import tech.mingle.basel.assignment.utils.AirportMapPoint

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(viewModel: MapViewModel) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var airportsResource by remember {
        mutableStateOf<Resource<List<Airport>>>(Resource.loading(null))
    }

    val mapCameraState by viewModel.mapCameraState.collectAsState(
        MapCameraState(
            Constants.INITIAL_MAP_LAT,
            Constants.INITIAL_MAP_LON,
            Constants.INITIAL_MAP_ZOOM_LEVEL
        )
    )

    val distanceUnit by viewModel.distanceUnit.collectAsState(null)

    LaunchedEffect(viewModel) {
        // Collecting the list as state triggers a recomposition, which causes the map to flicker.
        // Hence the remember/LaunchedEffect
        viewModel.getAirportList().collect {
            airportsResource = it
        }
    }

    val coroutineScope = rememberCoroutineScope()
    // Cached in rememberSaveable so that it survives configuration changes
    var selectedAirport by rememberSaveable { mutableStateOf<Airport?>(null) }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                val closestAirport by viewModel.getClosestAirport(selectedAirport)
                    .collectAsState(null)
                AirportDetails(airport = selectedAirport, closestAirport, distanceUnit)
            }
        },
        sheetShape = BottomSheetShape
    ) {
        when (airportsResource.status) {
            Resource.Status.SUCCESS -> {
                airportsResource.data?.let {
                    AirportMapView(
                        airports = it,
                        mapCameraState = mapCameraState,
                        onAirportClick = { airport ->
                            selectedAirport = airport
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                        onSaveState = { newMapCameraState ->
                            viewModel.saveMapCameraState(newMapCameraState)
                        }
                    )
                }
            }
            Resource.Status.ERROR -> {
                AssignmentAlertDialog(
                    title = "ERROR",
                    content = airportsResource.message ?: "Unknown Error",
                    dismissButtonText = "Dismiss"
                ) { }
            }
            Resource.Status.LOADING -> Progress()
        }
    }
}


@Composable
fun AirportMapView(
    airports: List<Airport>,
    mapCameraState: MapCameraState,
    onAirportClick: (airport: Airport) -> Unit,
    onSaveState: (MapCameraState) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnAirportClick by rememberUpdatedState(onAirportClick)
    val currentOnSaveState by rememberUpdatedState(onSaveState)

    val map = rememberMapViewWithLifecycle { mapView, event ->
        val collectCamState = {
            with(mapView.mapCenter) {
                MapCameraState(
                    latitude,
                    longitude,
                    mapView.zoomLevelDouble
                )
            }
        }

        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                mapView.onLifecycleResume()
            }
            Lifecycle.Event.ON_PAUSE -> {
                mapView.onLifecyclePause()
                currentOnSaveState(collectCamState())
            }
            Lifecycle.Event.ON_DESTROY -> {
                currentOnSaveState(collectCamState())
                mapView.onDetach()
            }
        }
    }

    SideEffect {
        val points = airports.map { airport ->
            AirportMapPoint(airport)
        }
        map.populateMarkers(points, currentOnAirportClick)
        map.translate(mapCameraState)
    }

    AndroidView(
        modifier = modifier,
        factory = { map }
    ) { mapView ->
        mapView.applyMapSettings()
    }
}


@Composable
fun Progress() {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
    )
}
