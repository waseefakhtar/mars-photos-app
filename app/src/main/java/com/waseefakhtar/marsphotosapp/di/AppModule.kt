package com.waseefakhtar.marsphotosapp.di

import com.waseefakhtar.marsphotosapp.common.Constants
import com.waseefakhtar.marsphotosapp.coroutines.DefaultDispatcherProvider
import com.waseefakhtar.marsphotosapp.coroutines.DispatcherProvider
import com.waseefakhtar.marsphotosapp.data.remote.NasaApi
import com.waseefakhtar.marsphotosapp.data.repository.MarsPhotosRepositoryImpl
import com.waseefakhtar.marsphotosapp.domain.repository.MarsPhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNasaApi(): NasaApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarsPhotosRepository(api: NasaApi): MarsPhotosRepository {
        return MarsPhotosRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}