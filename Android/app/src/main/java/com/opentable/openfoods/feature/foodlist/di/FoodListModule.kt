package com.opentable.openfoods.feature.foodlist.di

import com.opentable.openfoods.feature.foodlist.data.remote.FoodApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FoodListModule {

    @Provides
    @Singleton
    fun provideFoodListService(retrofit: Retrofit): FoodApiService {
        return retrofit.create(FoodApiService::class.java)
    }


}