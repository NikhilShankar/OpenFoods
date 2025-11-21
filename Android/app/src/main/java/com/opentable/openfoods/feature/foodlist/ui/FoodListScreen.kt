package com.opentable.openfoods.feature.foodlist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems


@Composable
fun FoodListScreenVM(modifier: Modifier,
                     foodListViewModel: FoodListViewModel = hiltViewModel()) {
    val state = foodListViewModel.foodListState.collectAsState()
    FoodListScreen(modifier,
        state = state.value,
        onEvent = { foodListViewModel.onEvent(it) }
    )
}

@Composable
fun FoodListScreen(modifier: Modifier,
                   state: FoodListScreenState,
                   onEvent: (FoodListEvent) -> Unit) {
    val foodItems = state.foodPager.collectAsLazyPagingItems()
    LazyColumn {
        items(foodItems.itemCount) { index ->
            foodItems[index]?.let {
                FoodItemCard(it ) {
                    onEvent.invoke(FoodListEvent.OnLike(it))
                }
            }
        }

        if (foodItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }

}