package tech.mingle.basel.assignment.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*

private const val PREFS_STORE_KEY = "PREFS_STORE"
private const val DISTANCE_UNIT_KEY = "DISTANCE_UNIT"

class PrefsStoreImpl : PrefsStore {

    private val distanceUnitPrefs = stringPreferencesKey(DISTANCE_UNIT_KEY)

    override fun distanceUnit(context: Context): Flow<DistanceUnit> =
        context.dataStore.data.catchIOExceptions().map { prefs ->
            prefs[distanceUnitPrefs]?.let { DistanceUnit.valueOf(it) }
                ?: DistanceUnit.KILOMETERS
        }

    override suspend fun setDistanceUnit(context: Context, unit: DistanceUnit) {
        context.dataStore.edit { data ->
            data[distanceUnitPrefs] = unit.name
        }
    }

    private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(PREFS_STORE_KEY)

    private fun Flow<Preferences>.catchIOExceptions(): Flow<Preferences> =
        catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
}