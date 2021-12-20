package tech.mingle.basel.assignment.data.repository

import androidx.paging.PagingSource
import tech.mingle.basel.assignment.api.AssignmentService
import tech.mingle.basel.assignment.data.dao.AssignmentDao
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.DistanceResult
import tech.mingle.basel.assignment.data.models.Flight
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentRepository @Inject constructor(
    private val assignmentService: AssignmentService,
    private val assignmentDao: AssignmentDao,
) {

    suspend fun loadInData(): Result<Unit> {
        return try {
            assignmentDao.onAirportsFetched(fetchAirports(), fetchFlights())
            Result.success(Unit)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure(exception)
        }
    }

    fun getAirportNamesByDistanceTo(airportId: String): PagingSource<Int, String> {
        return assignmentDao.getAirportNamesByDistance(airportId)
    }

    suspend fun getClosestAirport(airportId: String): DistanceResult {
        return assignmentDao.getClosestAirportName(airportId)
    }

    suspend fun getAllAirports(): List<Airport> {
        return assignmentDao.getAllAirports()
    }

    private suspend fun fetchAirports(): List<Airport> {
        return assignmentService.getAirports()
    }

    private suspend fun fetchFlights(): List<Flight> {
        return assignmentService.getFlights()
    }
}