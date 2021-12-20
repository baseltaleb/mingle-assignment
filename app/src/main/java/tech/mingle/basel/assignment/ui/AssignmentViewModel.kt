package tech.mingle.basel.assignment.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.mingle.basel.assignment.R
import tech.mingle.basel.assignment.data.models.Resource
import tech.mingle.basel.assignment.data.repository.AssignmentRepository
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    application: Application,
    private val repository: AssignmentRepository
) : AndroidViewModel(application) {

    private val _dataStatus = MutableLiveData<Resource<Unit>>()
    val dataStatus: LiveData<Resource<Unit>> = _dataStatus

    init {
        initData()
    }

    fun initData() {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        _dataStatus.postValue(Resource.loading(null))
        val res = withContext(Dispatchers.IO) {
            repository.loadInData()
        }
        if (res.isSuccess) {
            _dataStatus.postValue(Resource.success(null))
        } else {
            res.exceptionOrNull()?.printStackTrace()
            val message = if (res.exceptionOrNull() is UnknownHostException)
                getApplication<Application>().getString(R.string.internet_connection_error)
            else res.exceptionOrNull()?.localizedMessage ?: "Unknown error"

            _dataStatus.postValue(
                Resource.error(message, null)
            )
        }
    }

}