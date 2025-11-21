package com.opentable.openfoods.feature.foodlist.data.remote

import com.opentable.openfoods.feature.foodlist.models.FoodApiLikeUnlikeResponse
import com.opentable.openfoods.feature.foodlist.models.FoodApiListResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FoodApiService {

    @GET("{page}")
    suspend fun getFoodItems(@Path("page") page: Int): FoodApiListResponse

    @POST("{foodId}/like")
    suspend fun setLike(@Path("foodId") foodId: Int): FoodApiLikeUnlikeResponse


    @POST("{foodId}/unlike")
    suspend fun setUnlike(@Path("foodId") foodId: Int): FoodApiLikeUnlikeResponse

}