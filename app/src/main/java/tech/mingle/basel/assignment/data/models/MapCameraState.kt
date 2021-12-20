package tech.mingle.basel.assignment.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapCameraState(
    val latitude: Double,
    val longitude: Double,
    val zoomLevel: Double
    ) : Parcelable