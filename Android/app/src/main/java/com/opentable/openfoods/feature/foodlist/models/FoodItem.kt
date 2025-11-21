package com.opentable.openfoods.feature.foodlist.models

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Serializable
@Parcelize
data class FoodItem(
    val id: Int,
    val name: String,
    val isLiked: Boolean,
    val photoUrl: String?,
    val description: String?,
    //An ISO-3166-1 Alpha-2 country code ( eg. FR, IN etc )
    val countryOfOrigin: String?,
    //In ISO-8601 format
    val lastUpdatedDate: String?
) : Parcelable

@Serializable
data class FoodItemDto(
    val id: Int? = null,
    val name: String? = null,
    val isLiked: Boolean = false,
    val photoURL: String? = null,
    val description: String? = null,
    val countryOfOrigin: String? = null,
    val lastUpdatedDate: String? = null
)

fun FoodItemDto.toDomain(): FoodItem? {
    if(id == null || name == null) return null
    return FoodItem(
        id = id,
        name = name,
        isLiked = isLiked,
        photoUrl = photoURL,
        description = description,
        countryOfOrigin = countryOfOrigin,
        lastUpdatedDate = if(lastUpdatedDate != null) formatDate(lastUpdatedDate) else null
    )
}


//TODO Nikhil : Due to time constraints nit handling for api versions
//below 26. The other standard ways of conversion requires hardcoded strings
//which needs thorough testing. So in the interest of time falling back to this
//with no support for api versions 24 and 25
fun formatDate(isoDate: String): String? {
    return try {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null
        }
        val instant = Instant.parse(isoDate)

        val formatter = DateTimeFormatter
            .ofPattern("MMMM dd, yyyy") // "January 15, 2024"
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        null // Return original if parsing fails
    }
}