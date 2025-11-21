package com.opentable.openfoods.feature.foodlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(modifier, horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (foodItems.loadState.refresh == LoadState.Loading) {
                item {
                    ShowInitialLoading()
                }
            } else if (foodItems.loadState.refresh is LoadState.Error) {
                item {
                    ShowFullscreenError()
                }
            } else if (foodItems.itemCount == 0) {
                item {
                    ShowFullscreenNoItems()
                }
            } else {
                items(foodItems.itemCount) { index ->
                    foodItems[index]?.let { foodItem ->
                        FoodItemCard(foodItem) { clickedFoodItem ->
                            if (clickedFoodItem.isLiked) {
                                onEvent.invoke(FoodListEvent.OnUnlike(clickedFoodItem))
                            } else {
                                onEvent.invoke(FoodListEvent.OnLike(clickedFoodItem))
                            }
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
    }

}

@Composable
fun ShowInitialLoading() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.size(16.dp))
        Text("Open Food",
            modifier = Modifier.padding(48.dp),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(16.dp))
        Text("Yummy things taking a little time to load. :) !",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(48.dp),
            fontSize = 24.sp, fontWeight = FontWeight.Medium)
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
        Text("Open Food",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(48.dp),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(16.dp))
        Text("Something is not right !", modifier = Modifier.padding(48.dp), fontSize = 24.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.size(16.dp))
    }
}


@Composable
fun ShowFullscreenNoItems() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.size(16.dp))
        Text("Open Food",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(48.dp), fontSize = 32.sp, fontWeight = FontWeight.Medium)
        Text("Sorry there are no items here. \n Maybe its a good time to book a table in OpenTable ;) !",
            modifier = Modifier.padding(48.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.size(16.dp))
    }
}