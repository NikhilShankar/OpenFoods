package com.opentable.openfoods.feature.foodlist.data.remote

import com.opentable.openfoods.feature.foodlist.models.FoodApiResponse
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Polymorphic
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FoodApiService {

    @GET("{page}")
    suspend fun getFoodItems(@Path("page") page: Int): FoodApiResponse

    @POST("{foodId}/like")
    suspend fun setLike(@Path("foodId") foodId: Int): Any

    @POST("{foodId}/unlike")
    suspend fun setUnlike(@Path("foodId") foodId: Int): Any

}