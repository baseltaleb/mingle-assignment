package tech.mingle.basel.assignment.api

import retrofit2.http.GET
import tech.mingle.basel.assignment.data.Constants
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.Flight

interface AssignmentService {
    @GET(Constants.AIRPORTS_PATH)
    suspend fun getAirports(): List<Airport>

    @GET(Constants.FLIGHTS_PATH)
    suspend fun getFlights(): List<Flight>
}