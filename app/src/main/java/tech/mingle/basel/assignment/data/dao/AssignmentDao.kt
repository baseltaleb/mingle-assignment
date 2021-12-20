package tech.mingle.basel.assignment.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.AirportMapping
import tech.mingle.basel.assignment.data.models.DistanceResult
import tech.mingle.basel.assignment.data.models.Flight

@Dao
interface AssignmentDao {

    @Query("SELECT * FROM Airport")
    suspend fun getAllAirports(): List<Airport>

    @Query(
        """SELECT CASE WHEN a1.id = :airportId THEN a2.name ELSE a1.name END AS airportName, distance
                FROM AirportMapping
                LEFT JOIN Airport AS a1 ON AirportMapping.airportId1 = a1.mappingId
                LEFT JOIN Airport AS a2 ON AirportMapping.airportId2 = a2.mappingId
                WHERE a1.id = :airportId OR a2.id = :airportId
                ORDER BY distance ASC
                LIMIT 1"""
    )
    suspend fun getClosestAirportName(airportId: String): DistanceResult

    @Query(
        """SELECT CASE WHEN a1.id = :airportId THEN a2.name ELSE a1.name END
                FROM AirportMapping
                INNER JOIN (SELECT id FROM Flight WHERE departureAirportId = :airportId LIMIT 1)
                LEFT JOIN Airport AS a1 ON AirportMapping.airportId1 = a1.mappingId
                LEFT JOIN Airport AS a2 ON AirportMapping.airportId2 = a2.mappingId
                WHERE a1.id = :airportId OR a2.id = :airportId GROUP BY distance
                ORDER BY distance ASC"""
    )
    fun getAirportNamesByDistance(airportId: String): PagingSource<Int, String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAirports(airports: List<Airport>)

    suspend fun onAirportsFetched(airports: List<Airport>, flights: List<Flight>) {
        clear()
        insertAirports(airports)
        insertFlights(flights)
        // Create distances pivot table
        val airportsWithMappingId = getAllAirports()
        val mapping = airportsWithMappingId.map { airport ->
            AirportMapping.create(airport, airportsWithMappingId)
        }
        insertMapping(mapping.flatten())
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAirport(airport: Airport)

    @Insert(entity = AirportMapping::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMapping(mapping: AirportMapping)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMapping(mappingList: List<AirportMapping>)

    @Insert
    suspend fun insertFlights(flights: List<Flight>)

    @Query("DELETE FROM Airport")
    suspend fun clearAirports()

    @Query("DELETE FROM AirportMapping")
    suspend fun clearMapping()

    @Query("DELETE FROM Flight")
    suspend fun clearFlights()

    @Transaction
    suspend fun clear() {
        clearMapping()
        clearAirports()
        clearFlights()
    }
}