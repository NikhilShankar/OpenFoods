package com.opentable.openfoods.feature.foodlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.opentable.openfoods.feature.foodlist.data.remote.FoodRepository
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(val foodRepo: FoodRepository): ViewModel() {

    //Handles like / unlike on individual food item.
    //This map maintains any user actions and is required since
    //we wont be refreshing the food list upon each like / unlike.
    private val optimisticLikeState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val foodPager = combine(
        foodRepo.getFoodPager().cachedIn(viewModelScope),
        optimisticLikeState
    ) { pagingData, optimisticMap ->
        pagingData.map { item ->
            optimisticMap[item.id]?.let { item.copy(isLiked = it) } ?: item
        }
    }

    private var _foodListState: MutableStateFlow<FoodListScreenState> =
        MutableStateFlow(FoodListScreenState(foodPager = foodPager))
    val foodListState = _foodListState.asStateFlow()

    // Track items that are currently processing
    private val processingItems = MutableStateFlow<Set<Int>>(emptySet())


    //Side Effect for sending snackbar notif on like or unlike failure
    private val _sideEffectFlow = MutableSharedFlow<FoodListSideEffect?>(0)
    val sideEffectFlow = _sideEffectFlow.asSharedFlow()


    fun onEvent(event: FoodListEvent) {
        when(event) {
            is FoodListEvent.OnLike -> {
                // Ignore if already processing
                if (processingItems.value.contains(event.foodItem.id)) return
                handleLikeUnlike(event.foodItem, isLike = true)
            }
            is FoodListEvent.OnUnlike -> {
                // Ignore if already processing
                if (processingItems.value.contains(event.foodItem.id)) return
                handleLikeUnlike(event.foodItem, isLike = false)
            }
        }
    }

    private fun handleLikeUnlike(item: FoodItem, isLike: Boolean) {
        // Mark as processing
        processingItems.value = processingItems.value + item.id

        // Optimistic UI update
        optimisticLikeState.value = optimisticLikeState.value + (item.id to isLike)

        viewModelScope.launch {
            val success = if (isLike) {
                foodRepo.setLike(item).firstOrNull() ?: false
            } else {
                foodRepo.setUnLike(item).firstOrNull() ?: false
            }

            if (!success) {
                // Revert on failure
                optimisticLikeState.value = optimisticLikeState.value + (item.id to !isLike)
                _sideEffectFlow.emit(
                    FoodListSideEffect.OnLikeUnlikeFailure(
                        "Couldn't ${if (isLike) "like" else "unlike"} ${item.name}. Please try again!"
                    )
                )
            }
            // Remove from processing
            processingItems.value = processingItems.value - item.id
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

sealed class FoodListSideEffect {
    data class OnLikeUnlikeFailure(val userFacingMessage: String): FoodListSideEffect()
}