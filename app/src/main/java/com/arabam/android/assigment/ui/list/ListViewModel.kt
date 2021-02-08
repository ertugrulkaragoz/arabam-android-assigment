package com.arabam.android.assigment.ui.list

import androidx.lifecycle.*
import androidx.paging.*
import com.arabam.android.assigment.data.CarPagingSource
import com.arabam.android.assigment.data.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private var sort = DEFAULT_SORT_QUERY
    private var sortDirection = DEFAULT_SORT_DIRECTION_QUERY
    var minDate = DEFAULT_MIN_DATE
    var maxDate = DEFAULT_MAX_DATE
    var minYear = DEFAULT_MIN_YEAR
    var maxYear = DEFAULT_MAX_YEAR

    private val pagingConfig = PagingConfig(
        pageSize = 10,
        maxSize = 100,
        initialLoadSize = 10,
        enablePlaceholders = false
    )

    fun setSortQueries(sortQuery: Int, sortDirectionQuery: Int) {
        sort = sortQuery
        sortDirection = sortDirectionQuery
    }

    fun setFilterDateQueries(minDateQuery: String, maxDateQuery: String) {
        minDate = minDateQuery
        maxDate = maxDateQuery
    }

    fun setYearQueries(minYearQuery: Int, maxYearQuery: Int) {
        minYear = minYearQuery
        maxYear = maxYearQuery
    }

    val cars = Pager(pagingConfig) {
        CarPagingSource(
            repository,
            sort,
            sortDirection,
            minDate,
            maxDate,
            minYear,
            maxYear
        )
    }.liveData.cachedIn(viewModelScope)

    companion object {
        const val DEFAULT_SORT_QUERY = 1
        const val DEFAULT_SORT_DIRECTION_QUERY = 0
        const val DEFAULT_MIN_YEAR = 1970
        const val DEFAULT_MAX_YEAR = 2021
        const val DEFAULT_MIN_DATE = "1022-12-04T00:00:00"
        const val DEFAULT_MAX_DATE = "3022-12-04T00:00:00"
    }
}