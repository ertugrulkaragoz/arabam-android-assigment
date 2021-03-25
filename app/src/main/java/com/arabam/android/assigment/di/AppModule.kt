package com.arabam.android.assigment.di

import android.app.Application
import androidx.room.Room
import com.arabam.android.assigment.data.remote.CarApi
import com.arabam.android.assigment.data.local.CarListDao
import com.arabam.android.assigment.data.local.CarDatabase
import com.arabam.android.assigment.data.local.CarDetailDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(CarApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideCarApi(retrofit: Retrofit): CarApi =
        retrofit.create(CarApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CarDatabase =
        Room.databaseBuilder(app, CarDatabase::class.java, "car_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun carDAO(carDatabase: CarDatabase): CarListDao =
        carDatabase.carDao()

    @Provides
    @Singleton
    fun carDetailDAO(carDatabase: CarDatabase): CarDetailDao =
        carDatabase.carDetailDao()
}