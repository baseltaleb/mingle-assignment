package tech.mingle.basel.assignment.utils

import tech.mingle.basel.assignment.data.Constants
import tech.mingle.basel.assignment.data.models.Airport
import kotlin.math.*

fun Airport.distanceTo(other: Airport) : Double {
    val latDistance = Math.toRadians(other.latitude - latitude)
    val lonDistance = Math.toRadians(other.longitude - longitude)

    val a = sin(latDistance / 2).pow(2) +
            cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) *
            sin(lonDistance / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return c * Constants.EARTH_RADIUS_IN_KM
}

fun Double.toRoundedMiles(): Double =
    div(1.609).roundDistance()

fun Double.roundDistance(): Double =
    round(1)

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}