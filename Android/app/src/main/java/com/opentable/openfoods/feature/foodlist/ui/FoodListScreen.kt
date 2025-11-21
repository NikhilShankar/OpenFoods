package com.opentable.openfoods.feature.foodlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems


@Composable
fun FoodListScreenVM(modifier: Modifier,
                     foodListViewModel: FoodListViewModel = hiltViewModel(),
                     snackbarMessage: (String) -> Unit = {}) {

    LaunchedEffect(foodListViewModel) {
        foodListViewModel.sideEffectFlow.collect {
            when(it) {
                is FoodListSideEffect.OnLikeUnlikeFailure -> {
                    snackbarMessage.invoke(it.userFacingMessage)
                }
                null -> {

                }
            }
        }
    }

    val state = foodListViewModel.foodListState.collectAsState()
    FoodListScreen(modifier,
        state = state.value,
        onEvent = { foodListViewModel.onEvent(it) },
        snackbarMessage = snackbarMessage
    )
}

@Composable
fun FoodListScreen(modifier: Modifier,
                   state: FoodListScreenState,
                   onEvent: (FoodListEvent) -> Unit,
                   snackbarMessage: (String) -> Unit = {}) {
    val foodItems = state.foodPager.collectAsLazyPagingItems()
    LazyColumn(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        if (foodItems.loadState.refresh == LoadState.Loading) {
            item {
                ShowInitialLoading()
            }
        } else if (foodItems.loadState.refresh is LoadState.Error) {
            item {

            }
        }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }

}

@Composable
fun ShowInitialLoading() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.size(16.dp))
        Text("Yummy things taking a little time to load. :) !")
        Spacer(modifier = Modifier.size(16.dp))
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ShowFullscreenError() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.size(16.dp))
        Text("Something is not right !")
        Spacer(modifier = Modifier.size(16.dp))
    }
}