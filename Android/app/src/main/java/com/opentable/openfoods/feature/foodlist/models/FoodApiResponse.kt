package com.opentable.openfoods.feature.foodlist.models

import kotlinx.serialization.Serializable

@Serializable
data class FoodApiResponse(
    val foods: List<FoodItemDto>,
    val totalCount: Int
)