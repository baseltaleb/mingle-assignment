package tech.mingle.basel.assignment.ui.map

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme
import tech.mingle.basel.assignment.R
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.MapCameraState
import tech.mingle.basel.assignment.utils.AirportMapPoint

@Composable
fun rememberMapViewWithLifecycle(onEvent: (MapView, Lifecycle.Event) -> Unit): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
//            id = R.id.map

        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        // Make MapView follow the current lifecycle
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            onEvent(mapView, event)
        }

        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

fun MapView.applyMapSettings() {
    setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.BASE_OVERLAY_NL)
    with(org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2()) {
        setMultiplier(2f)
        setBackgroundColor(Color.TRANSPARENT)
        textPaint = Paint()
            .also { it.color = Color.BLACK; it.textSize = 16f }
        setLineColor(Color.parseColor("#55000000"))
        overlays.add(this)
    }
    setMultiTouchControls(true)
    minZoomLevel = 3.0
}

fun MapView.translate(mapCameraState: MapCameraState) {
    with(controller) {
        setZoom(mapCameraState.zoomLevel)
        setCenter(GeoPoint(mapCameraState.latitude, mapCameraState.longitude))
    }
}

/** OSM configurations and onPause callback  */
fun MapView.onLifecyclePause() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    Configuration.getInstance().save(context, prefs)
    onPause()
}

/** OSM configurations and onResume callback  */
fun MapView.onLifecycleResume() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    Configuration.getInstance().load(context, prefs)
    onResume()
}

fun MapView.populateFastMarkers(
    airportPoints: List<AirportMapPoint>,
    onClick: (airport: Airport) -> Unit
) {
    val pointTheme = SimplePointTheme(airportPoints, true)
    val textStyle = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
        textAlign = Paint.Align.CENTER
        textSize = 24f
    }
    val overlayOptions = SimpleFastPointOverlayOptions.getDefaultStyle()
        .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
        .setRadius(20f).setIsClickable(true).setCellSize(25)
        .setTextStyle(textStyle)

    val overlay = SimpleFastPointOverlay(pointTheme, overlayOptions)
    overlay.setOnClickListener { points, point ->
        val airport = (points[point] as AirportMapPoint).airport
        onClick(airport)
    }
    overlays.add(overlay)
}

fun MapView.populateMarkers(
    airportPoints: List<AirportMapPoint>,
    onClick: (airport: Airport) -> Unit
) {
    airportPoints.forEach { airportPoint ->
        val marker = Marker(this).apply {
            position = airportPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24)
            setOnMarkerClickListener { marker, mapView ->
                onClick(airportPoint.airport)
                true
            }
        }
        overlays.add(marker)
    }
}