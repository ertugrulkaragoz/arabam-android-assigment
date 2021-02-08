package com.arabam.android.assigment.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arabam.android.assigment.data.model.Car

class CarPagingSource(
    private val repository: CarRepository,
    private val sortQuery: Int,
    private val sortDirectionQuery: Int,
    private val minDate: String,
    private val maxDate: String,
    private val minYear: Int,
    private val maxYear: Int
) : PagingSource<Int, Car>() {

    object EmptyListException : Exception()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Car> {
        val skip = params.key ?: STARTING_PAGE_INDEX
        val response = repository.getCars(
            sortQuery,
            sortDirectionQuery,
            minDate,
            maxDate,
            minYear,
            maxYear,
            skip,
            params.loadSize
        )

        return when (response) {
            is ResultWrapper.Success -> LoadResult.Page(
                data = response.value,
                prevKey = if (skip == STARTING_PAGE_INDEX) null else skip - params.loadSize,
                nextKey = if (response.value.isEmpty()) null else skip + params.loadSize
            )
            is ResultWrapper.NetworkError -> LoadResult.Error(EmptyListException)
            is ResultWrapper.GenericError -> LoadResult.Error(EmptyListException)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Car>): Int? {
        return null
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 0
    }
}