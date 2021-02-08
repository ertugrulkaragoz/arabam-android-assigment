package com.arabam.android.assigment.api

import com.arabam.android.assigment.data.model.Car
import com.arabam.android.assigment.data.model.CarDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface CarApi {

    @GET("api/v1/listing")
    suspend fun getCars(
        @Query("sort") sort: Int,
        @Query("sortDirection") sortDirection: Int,
        @Query("minDate") minDate: String,
        @Query("maxDate") maxDate: String,
        @Query("minYear") minYear: Int,
        @Query("maxYEar") maxYear: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int
    ): List<Car>

    @GET("api/v1/detail")
    suspend fun getCarDetail(
        @Query("id") id: Int
    ): CarDetail

    companion object {
        const val BASE_URL = "http://sandbox.arabamd.com/"
    }
}