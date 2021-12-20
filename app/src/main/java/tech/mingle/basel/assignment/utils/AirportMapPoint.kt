package tech.mingle.basel.assignment.utils

import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
import tech.mingle.basel.assignment.data.models.Airport

class AirportMapPoint(val airport: Airport) : LabelledGeoPoint(airport.latitude, airport.longitude, airport.name)
