package tech.mingle.basel.assignment.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flight(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val airlineId: String,
    val arrivalAirportId: String,
    val departureAirportId: String,
    val flightNumber: Int,
)