package com.magnum_ti1.plant.presentation.content.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.domain.plant.PlantUseCase
import kotlinx.coroutines.Dispatchers

class DetailViewModel(private val plantUseCase: PlantUseCase) : ViewModel() {
    fun getDetailPlantList(plantId: Int?) = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            emit(plantUseCase.getDetailPlantList(plantId))
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }
}