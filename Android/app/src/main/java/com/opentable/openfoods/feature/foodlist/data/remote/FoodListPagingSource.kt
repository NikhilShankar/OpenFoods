package com.opentable.openfoods.feature.foodlist.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import com.opentable.openfoods.feature.foodlist.models.toDomain

class FoodListPagingSource (
    private val apiService: FoodApiService
) : PagingSource<Int, FoodItem>() {

    var pageSize: Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FoodItem> {
        return try {
            val page = params.key ?: 0
            val response = apiService.getFoodItems(page)
            if(pageSize == null) {
                pageSize = response.foods.size
            }
            val totalPages = (response.totalCount + (pageSize ?: 10) - 1) / (pageSize ?: 10)
            LoadResult.Page(
                data = response.foods.mapNotNull { it.toDomain() },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page < totalPages - 1) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FoodItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            page
        }
    }


}