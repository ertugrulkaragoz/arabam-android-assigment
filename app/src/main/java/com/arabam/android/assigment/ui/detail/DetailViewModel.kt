package com.arabam.android.assigment.ui.detail

import androidx.lifecycle.*
import com.arabam.android.assigment.data.CarRepository
import com.arabam.android.assigment.data.ResultWrapper
import com.arabam.android.assigment.data.model.CarDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _carDetail = MutableLiveData<ResultWrapper<CarDetail>>()
    val carDetail: LiveData<ResultWrapper<CarDetail>> get() = _carDetail

    fun getCarDetail(id: Int) {
        viewModelScope.launch {
            _carDetail.postValue(repository.getCarDetail(id))
        }
    }
}