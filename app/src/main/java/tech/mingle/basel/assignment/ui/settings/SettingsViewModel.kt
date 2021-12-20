package tech.mingle.basel.assignment.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import tech.mingle.basel.assignment.data.preferences.DistanceUnit
import tech.mingle.basel.assignment.data.preferences.PrefsStore
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(application: Application, val prefsStore: PrefsStore) :
    AndroidViewModel(application) {

    val distanceUnit: Flow<DistanceUnit> = prefsStore.distanceUnit(getApplication())

    fun setDistanceUnit(distanceUnit: DistanceUnit) {
        viewModelScope.launch {
            prefsStore.setDistanceUnit(getApplication(), distanceUnit)
        }
    }

}