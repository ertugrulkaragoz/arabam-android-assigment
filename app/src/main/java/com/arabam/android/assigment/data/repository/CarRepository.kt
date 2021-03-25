package com.arabam.android.assigment.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import com.arabam.android.assigment.data.remote.CarApi
import com.arabam.android.assigment.data.local.CarRemoteMediator
import com.arabam.android.assigment.data.local.CarDatabase
import com.arabam.android.assigment.data.model.CarEntity
import com.arabam.android.assigment.data.model.Filter
import com.arabam.android.assigment.data.model.Sort
import com.arabam.android.assigment.util.networkBoundSource
import com.arabam.android.assigment.util.toEntity
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Singleton
class CarRepository @Inject constructor(
    private val api: CarApi,
    private val carDatabase: CarDatabase,
    moshi: Moshi
) : BaseRepository(moshi) {

    private var carDao = carDatabase.carDao()
    private var carDetailDao = carDatabase.carDetailDao()

    fun getCarDetail(id: Long) = networkBoundSource(
        query = { carDetailDao.getCarDetail(id) },
        fetch = { safeApiCall { api.getCarDetail(id) } },
        saveFetchResult = {
            carDatabase.withTransaction {
                it.data?.toEntity()?.let { it1 -> carDetailDao.insertCarDetail(it1) }
            }
        }
    )

    fun getCars(
        sort: Sort,
        filter: Filter,
        refreshOnInit: Boolean
    ): Pager<Int, CarEntity> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = CarRemoteMediator(
                api,
                carDatabase,
                sort,
                filter,
                refreshOnInit
            ),
            pagingSourceFactory = { carDao.getAllCarsPaged() }
        )
}