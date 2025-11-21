package com.opentable.openfoods.feature.foodlist.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FoodRepository @Inject constructor(private val foodApiService: FoodApiService) {

    fun getFoodPager(): Flow<PagingData<FoodItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            pagingSourceFactory = { FoodListPagingSource(foodApiService) }
        ).flow
    }

    fun setLike(item: FoodItem): Flow<Boolean> {
        return flow {
            try {
                val response = foodApiService.setLike(item.id)
                emit(true)
            } catch (e: Exception) {
                emit(false)
            }
        }
    }


    fun setUnLike(item: FoodItem): Flow<Boolean> {
        return flow {
            try {
                val response = foodApiService.setUnlike(item.id)
                emit(true)
            } catch (e: Exception) {
                emit(false)
            }
        }
    }

}