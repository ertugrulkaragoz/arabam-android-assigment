package com.arabam.android.assigment.data.local

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.arabam.android.assigment.data.remote.CarApi
import com.arabam.android.assigment.data.model.CarEntity
import com.arabam.android.assigment.data.model.SkipEntity
import com.arabam.android.assigment.data.model.Filter
import com.arabam.android.assigment.data.model.Sort
import com.arabam.android.assigment.util.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CarRemoteMediator(
    private val carApi: CarApi,
    private val carDatabase: CarDatabase,
    private val sort: Sort,
    private val filter: Filter,
    private val refreshOnInit: Boolean
) : RemoteMediator<Int, CarEntity>() {

    private val carDao = carDatabase.carDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CarEntity>
    ): MediatorResult {

        return try {
            val skip = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                LoadType.APPEND -> withContext(Dispatchers.IO) { carDao.getLastSkip() }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }
            val cars = carApi.getCars(
                sort.sort,
                sort.sortDirection,
                filter.minDate,
                filter.maxDate,
                filter.minYear,
                filter.maxYear,
                skip,
                state.config.pageSize,
            )

            carDatabase.withTransaction {
                if (LoadType.REFRESH == loadType) {
                    carDao.deleteAllCars()
                    carDao.deleteAllSkips()
                }

                val nextSkip = skip + state.config.pageSize
                val carEntities = cars.map { car ->
                    car.toEntity()
                }

                carDao.insertSkip(SkipEntity(nextSkip))
                carDao.insertCar(carEntities)
            }
            MediatorResult.Success(endOfPaginationReached = cars.isEmpty())
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return if (refreshOnInit) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 0
    }
}