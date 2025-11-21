package com.opentable.openfoods

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.opentable.openfoods.feature.foodlist.data.remote.FoodRepository
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import com.opentable.openfoods.feature.foodlist.ui.FoodListEvent
import com.opentable.openfoods.feature.foodlist.ui.FoodListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

//TODO Nikhil : Couldnt complete the unit test cases properly.
//Currently the test cases are half cooked and buggy. Need to figure out how to test
//paging3 properly using mockK.
@OptIn(ExperimentalCoroutinesApi::class)
class FoodListViewModelTest {

    private lateinit var viewModel: FoodListViewModel
    private lateinit var mockRepository: FoodRepository
    private val testDispatcher = StandardTestDispatcher()

    private val testFoodItems = listOf(
        FoodItem(
            id = 5,
            name = "Pizza",
            isLiked = false,
            photoUrl = "",
            description = null,
            countryOfOrigin = null,
            lastUpdatedDate = ""
        ),
        FoodItem(id = 6, name = "Burger", isLiked = false, photoUrl = "", description = null, countryOfOrigin = null, lastUpdatedDate = ""),
        FoodItem(id = 7, name = "Pasta", isLiked = true, photoUrl = "", description = null, countryOfOrigin = null, lastUpdatedDate = "")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        // Mock the paging data
        coEvery { mockRepository.getFoodPager() } returns flowOf(
            PagingData.from(testFoodItems)
        )
        coEvery { mockRepository.setLike(any()) } returns flowOf(true)
        coEvery { mockRepository.setUnLike(any()) } returns flowOf(true)
        viewModel = FoodListViewModel(mockRepository)
    }


    @Test
    fun `test like event correctly triggers api call`() = runTest {
        coEvery { mockRepository.setLike(any()) } returns flowOf(true)
        val itemToLike = testFoodItems[0] // id = 5, isLiked = false

        // Create a test collector
        val collectedItems = mutableListOf<List<FoodItem>>()
        val job = launch {
            viewModel.foodPager.collect { pagingData ->
                // You can't easily extract items from PagingData
                // So let's test what we CAN test
            }
        }

        // Trigger the event
        viewModel.onEvent(FoodListEvent.OnLike(itemToLike))
        advanceUntilIdle()

        // Verify API was called
        coVerify { mockRepository.setLike(itemToLike) }

        job.cancel()
    }

    @Test
    fun `test unlike event correctly triggers unlike api call`() = runTest {
        coEvery { mockRepository.setLike(any()) } returns flowOf(true)
        val itemToLike = testFoodItems[0] // id = 5, isLiked = false

        // Create a test collector
        val collectedItems = mutableListOf<List<FoodItem>>()
        val job = launch {
            viewModel.foodPager.collect { pagingData ->
                // You can't easily extract items from PagingData
                // So let's test what we CAN test
            }
        }

        // Trigger the event
        viewModel.onEvent(FoodListEvent.OnUnlike(itemToLike))
        advanceUntilIdle()

        // Verify API was called
        coVerify { mockRepository.setUnLike(itemToLike) }

        job.cancel()
    }

    @Test
    fun `test like event correctly triggers api call once`() = runTest {
        coEvery { mockRepository.setLike(any()) } returns flowOf(true)
        val itemToLike = testFoodItems[0] // id = 5, isLiked = false

        // Create a test collector
        val collectedItems = mutableListOf<List<FoodItem>>()
        val job = launch {
            viewModel.foodPager.collect { pagingData ->
                // You can't easily extract items from PagingData
                // So let's test what we CAN test
            }
        }

        // Trigger the event
        viewModel.onEvent(FoodListEvent.OnLike(itemToLike))
        viewModel.onEvent(FoodListEvent.OnLike(itemToLike))
        viewModel.onEvent(FoodListEvent.OnLike(itemToLike))
        advanceUntilIdle()

        // Verify API was called
        coVerify(atLeast = 1, atMost = 1) { mockRepository.setLike(itemToLike) }

        job.cancel()
    }

    @Test
    fun `test like and unlike event correctly triggers api call twice`() = runTest {
        coEvery { mockRepository.setLike(any()) } returns flowOf(true)
        coEvery { mockRepository.setUnLike(any()) } returns flowOf(true)
        val itemToLike = testFoodItems[0]
        val itemToUnLike = testFoodItems[2]
        // Trigger the event
        viewModel.onEvent(FoodListEvent.OnLike(itemToLike))
        viewModel.onEvent(FoodListEvent.OnUnlike(itemToLike))
        advanceUntilIdle()

        // Verify API was called
        coVerify(atLeast = 1, atMost = 1) { mockRepository.setLike(itemToLike) }
        coVerify(atLeast = 1, atMost = 1) { mockRepository.setUnLike(itemToLike) }
    }




}