package tech.mingle.basel.assignment.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import tech.mingle.basel.assignment.utils.distanceTo

@Entity(
    primaryKeys = ["airportId1", "airportId2"],
    foreignKeys = [
        ForeignKey(
            entity = Airport::class,
            parentColumns = ["mappingId"],
            childColumns = ["airportId1"]
        ),
        ForeignKey(
            entity = Airport::class,
            parentColumns = ["mappingId"],
            childColumns = ["airportId2"]
        ),
    ]
)
data class AirportMapping(
    val airportId1: Long,
    val airportId2: Long,
    val distance: Double
) {
    companion object {
        fun create(airport: Airport, airports: List<Airport>): List<AirportMapping> {
            return airports.mapNotNull { airportInList ->
                // Insure no two ids are repeated in a given row
                if (airport.mappingId < airportInList.mappingId) {
                    AirportMapping(
                        airport.mappingId,
                        airportInList.mappingId,
                        airport.distanceTo(airportInList)
                    )
                }
                else {
                    null
                }
            }
        }
    }
}