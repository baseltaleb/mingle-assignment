package tech.mingle.basel.assignment.ui.common

import android.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import tech.mingle.basel.assignment.data.models.MapCameraState

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

fun MapView.applyConfigurations() {
    setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.BASE_OVERLAY_NL)
    with(org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2()) {
        setMultiplier(2f)
        setBackgroundColor(Color.TRANSPARENT)
        textPaint = android.graphics.Paint()
            .also { it.color = Color.BLACK; it.textSize = 16f }
        setLineColor(Color.parseColor("#55000000"))
        overlays.add(this)
    }
    setMultiTouchControls(true)
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
