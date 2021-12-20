package tech.mingle.basel.assignment.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val mappingId: Long,
    val id: String,
    val city: String,
    val countryId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
) : Parcelable