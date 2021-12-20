package tech.mingle.basel.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.mingle.basel.assignment.api.AssignmentService

@RunWith(JUnit4::class)
class ServiceTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: AssignmentService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssignmentService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getAirports() = runBlocking {
        enqueueResponse("mock-airports.json")
        val airports = service.getAirports()
        assert(airports.size == 3)
        val airport = airports.first()
        assert(airport.id == "A")
        assert(airport.name == "Airport A")
        assert(airport.city == "City A")
        assert(airport.countryId == "A")
        assert(airport.latitude == 0.0)
        assert(airport.longitude == 0.0)
    }

    @Test
    fun getFlights() = runBlocking {
        enqueueResponse("mock-flights.json")
        val flights = service.getFlights()
        assert(flights.size == 3)
        val flight = flights.first()
        assert(flight.airlineId == "1")
        assert(flight.flightNumber == 1)
        assert(flight.departureAirportId == "A")
        assert(flight.arrivalAirportId == "B")
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}