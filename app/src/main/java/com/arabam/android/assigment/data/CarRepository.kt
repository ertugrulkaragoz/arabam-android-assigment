package com.arabam.android.assigment.data

import com.arabam.android.assigment.api.CarApi
import com.arabam.android.assigment.data.model.Car
import com.arabam.android.assigment.data.model.CarDetail
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(
    private val api: CarApi,
    moshi: Moshi
) : BaseRepository(moshi) {

    suspend fun getCarDetail(id: Int): ResultWrapper<CarDetail> {
        return safeApiCall(Dispatchers.IO) { api.getCarDetail(id) }
    }

    suspend fun getCars(
        sort: Int,
        sortDirection: Int,
        minDate: String,
        maxDate: String,
        minYear: Int,
        maxYear: Int,
        skip: Int,
        take: Int
    ): ResultWrapper<List<Car>> {
        return safeApiCall(Dispatchers.IO) {
            api.getCars(
                sort,
                sortDirection,
                minDate,
                maxDate,
                minYear,
                maxYear,
                skip,
                take
            )
        }
    }
}