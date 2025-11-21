package com.opentable.openfoods.feature.foodlist.models

import kotlinx.serialization.Serializable

@Serializable
data class FoodApiListResponse(
    val foods: List<FoodItemDto>,
    val totalCount: Int
)

@Serializable
data class FoodApiLikeUnlikeResponse(
    val success: Boolean
)
