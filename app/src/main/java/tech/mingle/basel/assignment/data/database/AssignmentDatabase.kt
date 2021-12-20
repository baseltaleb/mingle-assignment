package tech.mingle.basel.assignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.mingle.basel.assignment.data.dao.AssignmentDao
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.AirportMapping
import tech.mingle.basel.assignment.data.models.Flight

@Database(entities = [Airport::class, Flight::class, AirportMapping::class], version = 1)
abstract class AssignmentDatabase : RoomDatabase() {
    abstract fun assignmentDao(): AssignmentDao
}