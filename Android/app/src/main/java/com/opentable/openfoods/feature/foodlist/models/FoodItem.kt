package com.opentable.openfoods.feature.foodlist.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

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
        lastUpdatedDate = lastUpdatedDate
    )
}