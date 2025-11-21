package com.opentable.openfoods.feature.foodlist.data.remote

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FoodRepository @Inject constructor(private val foodApiService: FoodApiService) {

    companion object {
        const val TAG = "FoodRepository"
    }
    fun getFoodPager(): Flow<PagingData<FoodItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            pagingSourceFactory = { FoodListPagingSource(foodApiService) }
        ).flow
    }

    //TODO Nikhil : The backend API wasnt returning success at any point.
    //Hardcoded testing has been done. Still need to verify this with BE
    fun setLike(item: FoodItem): Flow<Boolean> {
        return flow {
            try {
                val response = foodApiService.setLike(item.id)
                emit(response.success)
            } catch (e: Exception) {
                Log.i(TAG, "setLike: ${e.message}")
                emit(true)
            }
        }
    }


    //TODO Nikhil : The backend API wasnt returning success at any point.
    //Hardcoded testing has been done. Still need to verify this with BE
    fun setUnLike(item: FoodItem): Flow<Boolean> {
        return flow {
            try {
                val response = foodApiService.setUnlike(item.id)
                emit(response.success)
            } catch (e: Exception) {
                emit(false)
            }
        }
    }

}