package tech.mingle.basel.assignment.ui.airports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import tech.mingle.basel.assignment.data.repository.AssignmentRepository
import javax.inject.Inject

@HiltViewModel
class AirportListViewModel @Inject constructor(val repository: AssignmentRepository) : ViewModel() {
    fun getPagedAirportsByDistance(airportId: String): Flow<PagingData<String>> {
        return Pager(PagingConfig(pageSize = 20)) {
            repository.getAirportNamesByDistanceTo(airportId)
        }.flow.cachedIn(viewModelScope)
    }
}