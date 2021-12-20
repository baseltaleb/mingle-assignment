package tech.mingle.basel.assignment.ui.map

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tech.mingle.basel.assignment.data.models.Airport
import tech.mingle.basel.assignment.data.models.DistanceResult
import tech.mingle.basel.assignment.data.models.MapCameraState
import tech.mingle.basel.assignment.data.models.Resource
import tech.mingle.basel.assignment.data.preferences.DistanceUnit
import tech.mingle.basel.assignment.data.preferences.PrefsStore
import tech.mingle.basel.assignment.data.repository.AssignmentRepository
import javax.inject.Inject

private const val MAP_CAMERA_STATE_HANDLE_KEY = "MAP_CAMERA_STATE_HANDLE_KEY"

@HiltViewModel
class MapViewModel @Inject constructor(
    application: Application,
    private val repository: AssignmentRepository,
    private val prefsStore: PrefsStore,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    val distanceUnit: Flow<DistanceUnit> = prefsStore.distanceUnit(getApplication())


    private val _mapCameraState =
        savedStateHandle.getLiveData<MapCameraState>(MAP_CAMERA_STATE_HANDLE_KEY, )
    val mapCameraState: Flow<MapCameraState> = _mapCameraState.asFlow()

    fun getAirportList(): Flow<Resource<List<Airport>>> = flow {
        emit(Resource.loading(null))
        try {
            val airports = withContext(Dispatchers.IO) {
                repository.getAllAirports()
            }

            emit(Resource.success(airports))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(e.localizedMessage, null))
        }
    }

    fun saveMapCameraState(state: MapCameraState) {
        savedStateHandle.set(MAP_CAMERA_STATE_HANDLE_KEY, state)
    }

    fun getClosestAirport(airport: Airport?): Flow<DistanceResult> = flow {
        airport?.let {
            emit(repository.getClosestAirport(it.id))
        }
    }

}