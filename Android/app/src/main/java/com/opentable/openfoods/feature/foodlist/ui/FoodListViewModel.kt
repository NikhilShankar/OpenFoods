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

    // We dont want the network api for like or unlike getting called multiple times
    //in case user clicks on it multiple times randomly. Debounce of 400ms is introduced
    //to handle this. But this has to be per item. If user clicks on like on 2 items
    //within 400ms then we need to avoid debouncing. Hence the map which has id as key and and job
    //it has as value so that we can cancel the previous one if user clicks within 400ms.
    private val pendingJobs = mutableMapOf<Int, Job>()


    //Side Effect for sending snackbar notif on like or unlike failure
    private val _sideEffectFlow = MutableSharedFlow<FoodListSideEffect?>(0)
    val sideEffectFlow = _sideEffectFlow.asSharedFlow()


    fun onEvent(event: FoodListEvent) {
        when(event) {
            is FoodListEvent.OnLike -> {
                handleUiUpdate(event.foodItem, isLike = true)
                handleDebouncedUpdateRemote(event.foodItem, isLike = true)
            }
            is FoodListEvent.OnUnlike -> {
                handleUiUpdate(event.foodItem, false)
                handleDebouncedUpdateRemote(event.foodItem, isLike = false)
            }
        }
    }

    private fun handleUiUpdate(item: FoodItem, isLike: Boolean) {
        optimisticLikeState.value = optimisticLikeState.value + (item.id to isLike)
    }

    private fun handleDebouncedUpdateRemote(item: FoodItem, isLike: Boolean) {
        pendingJobs[item.id]?.cancel()
        pendingJobs[item.id] = viewModelScope.launch {
            kotlinx.coroutines.delay(400)
            if (isLike) {
                foodRepo.setLike(item).collect { result ->
                    //Handling Failure case
                    if(!result) {
                        handleUiUpdate(item, false)
                        _sideEffectFlow.emit(FoodListSideEffect.OnLikeUnlikeFailure("Couldn't like ${item.name}. Please try again !"))
                    }
                }
            } else {
                foodRepo.setUnLike(item).collect { result ->
                    //Handling Failure
                    if(!result) {
                        handleUiUpdate(item, true)
                        _sideEffectFlow.emit(FoodListSideEffect.OnLikeUnlikeFailure("Couldn't unlike ${item.name}. Please try again !"))
                    }
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

sealed class FoodListSideEffect {
    data class OnLikeUnlikeFailure(val userFacingMessage: String): FoodListSideEffect()
}