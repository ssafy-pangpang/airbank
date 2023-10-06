package com.example.myapplication.module


import com.example.myapplication.api.ApiService
import com.example.myapplication.network.RetrofitBuilder
import com.example.myapplication.repository.LoanRepository
import com.example.myapplication.repository.SavingsRepository
import com.example.myapplication.repository.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitBuilder.createApiService()
}
