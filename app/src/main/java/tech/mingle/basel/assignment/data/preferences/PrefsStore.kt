package tech.mingle.basel.assignment.data.preferences

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.*


interface PrefsStore {
    fun distanceUnit(context: Context): Flow<DistanceUnit>
    suspend fun setDistanceUnit(context: Context, unit: DistanceUnit)
}