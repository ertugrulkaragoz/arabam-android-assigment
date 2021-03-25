package com.arabam.android.assigment.ui.list

import androidx.lifecycle.*
import androidx.paging.*
import com.arabam.android.assigment.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.arabam.android.assigment.data.model.Filter
import com.arabam.android.assigment.data.model.Sort

@HiltViewModel
class ListViewModel @Inject constructor(
    repository: CarRepository
) : ViewModel() {

    var sort = Sort(DEFAULT_SORT_QUERY, DEFAULT_SORT_DIRECTION_QUERY)
    var filter = Filter(DEFAULT_MIN_DATE, DEFAULT_MAX_DATE, DEFAULT_MIN_YEAR, DEFAULT_MAX_YEAR)

    private var refreshOnInit = false
    var newQueryInProgress = false

    fun setSortQueries(sortQuery: Int, sortDirectionQuery: Int) {
        sort.sort = sortQuery
        sort.sortDirection = sortDirectionQuery
        refreshOnInit = true
        newQueryInProgress = true
    }

    fun setFilterDateQueries(minDateQuery: String, maxDateQuery: String) {
        filter.minDate = minDateQuery
        filter.maxDate = maxDateQuery
        refreshOnInit = true
        newQueryInProgress = true
    }

    fun setYearQueries(minYearQuery: Int, maxYearQuery: Int) {
        filter.minYear = minYearQuery
        filter.maxYear = maxYearQuery
        refreshOnInit = true
        newQueryInProgress = true
    }

    val cars = repository.getCars(
        sort,
        filter,
        refreshOnInit
    ).liveData.cachedIn(viewModelScope)

    companion object {
        const val DEFAULT_SORT_QUERY = 1
        const val DEFAULT_SORT_DIRECTION_QUERY = 0
        const val DEFAULT_MIN_YEAR = 1970
        const val DEFAULT_MAX_YEAR = 2021
        const val DEFAULT_MIN_DATE = "1022-12-04T00:00:00"
        const val DEFAULT_MAX_DATE = "3022-12-04T00:00:00"
    }
}