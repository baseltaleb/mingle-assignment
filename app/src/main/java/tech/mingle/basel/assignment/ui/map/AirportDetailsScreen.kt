package tech.mingle.basel.assignment.ui.map

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.mingle.basel.assignment.R
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.DistanceResult
import tech.mingle.basel.assignment.data.preferences.DistanceUnit
import tech.mingle.basel.assignment.utils.roundDistance
import tech.mingle.basel.assignment.utils.toRoundedMiles

@Composable
fun AirportDetails(
    airport: Airport?,
    closestAirportName: DistanceResult?,
    distanceUnit: DistanceUnit?
) {
    val unitsAbbreviation: String
    val distance: Double

    if (distanceUnit == DistanceUnit.MILES) {
        distance = closestAirportName?.distance?.toRoundedMiles() ?: 0.0
        unitsAbbreviation = stringResource(R.string.distance_unit_abbreviation_ml)
    } else {
        distance = closestAirportName?.distance?.roundDistance() ?: 0.0
        unitsAbbreviation = stringResource(R.string.distance_unit_abbreviation_km)
    }


    if (airport == null) {
        Text("No selected airport")
    } else {
        Column {
            Text(airport.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            AirportDetailsItem(strRes = R.string.airport_details_airport_id, value = airport.id)
            AirportDetailsItem(strRes = R.string.airport_details_city, value = airport.city)
            AirportDetailsItem(strRes = R.string.airport_details_country, value = airport.countryId)
            AirportDetailsItem(
                strRes = R.string.airport_details_latitude,
                value = airport.latitude.toString()
            )
            AirportDetailsItem(
                strRes = R.string.airport_details_longitude,
                value = airport.longitude.toString()
            )

            closestAirportName?.let {
                AirportDetailsItem(
                    strRes = R.string.airport_details_nearest_airport,
                    value = it.airportName
                )
                AirportDetailsItem(
                    strRes = R.string.airport_details_nearest_airport,
                    value = "$distance $unitsAbbreviation"
                )
            }
        }
    }
}

@Composable
fun AirportDetailsItem(@StringRes strRes: Int, value: String) {
    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${stringResource(strRes)}: ",
            modifier = Modifier.alpha(0.6f)
        )
        Text(text = value)
    }
}

@Preview(showBackground = true)
@Composable
fun AirPortDetailsPreview() {
    AirportDetails(
        Airport(0, "id", "City", "Country id", 0.0, 0.0, "Preview airport"),
        DistanceResult("Airport name", 10.0),
        DistanceUnit.KILOMETERS
    )
}
