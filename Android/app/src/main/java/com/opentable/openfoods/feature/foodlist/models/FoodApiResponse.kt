package com.opentable.openfoods.feature.foodlist.models

data class FoodApiResponse(
    val foods: List<FoodItemDto>,
    val totalCount: Int
)