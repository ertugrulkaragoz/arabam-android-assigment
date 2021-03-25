package com.arabam.android.assigment.ui.detail

import androidx.lifecycle.*
import com.arabam.android.assigment.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    var id = MutableLiveData<Long>()

    var carDetail = id.switchMap {
        repository.getCarDetail(it).asLiveData()
    }

    fun setId(id: Long) {
        this.id.value = id
    }
}