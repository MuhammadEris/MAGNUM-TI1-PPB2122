package com.magnum_ti1.plant.presentation.content.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.domain.plant.PlantUseCase
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val plantUseCase: PlantUseCase) : ViewModel() {
    fun getPlantList() = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            emit(plantUseCase.getPlantList())
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }
}