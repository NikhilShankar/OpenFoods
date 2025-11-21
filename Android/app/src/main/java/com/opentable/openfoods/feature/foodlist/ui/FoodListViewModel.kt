package com.opentable.openfoods.feature.foodlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.opentable.openfoods.feature.foodlist.data.remote.FoodRepository
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(val foodRepo: FoodRepository): ViewModel() {

    private var _foodListState: MutableStateFlow<FoodListScreenState> =
        MutableStateFlow(FoodListScreenState(foodPager = foodRepo.getFoodPager().cachedIn(viewModelScope)))
    val foodListState = _foodListState.asStateFlow()

    // Track pending jobs per item ID for debouncing
    private val pendingJobs = mutableMapOf<Int, Job>()

    fun onEvent(event: FoodListEvent) {
        when(event) {
            is FoodListEvent.OnLike -> {
                handleDebounced(event.foodItem, isLike = true)
            }
            is FoodListEvent.OnUnlike -> {
                handleDebounced(event.foodItem, isLike = false)
            }
        }
    }

    private fun handleDebounced(item: FoodItem, isLike: Boolean) {
        pendingJobs[item.id]?.cancel()
        pendingJobs[item.id] = viewModelScope.launch {
            kotlinx.coroutines.delay(400)
            if (isLike) {
                foodRepo.setLike(item).collect { result ->

                }
            } else {
                foodRepo.setUnLike(item).collect { result ->

                }
            }
            pendingJobs.remove(item.id)
        }
    }


}

sealed class FoodListEvent {
    data class OnLike(val foodItem: FoodItem): FoodListEvent()
    data class OnUnlike(val foodItem: FoodItem): FoodListEvent()
}


data class FoodListScreenState(
    val foodPager: Flow<PagingData<FoodItem>>
)