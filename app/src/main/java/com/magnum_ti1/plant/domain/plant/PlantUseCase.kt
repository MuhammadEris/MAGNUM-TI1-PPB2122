package com.magnum_ti1.plant.domain.plant

import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.Plant
import com.magnum_ti1.plant.data.entity.PlantDetail

interface PlantUseCase {
    suspend fun getPlantList(): Response<List<Plant>>
    suspend fun getDetailPlantList(plantId: Int?): Response<List<PlantDetail>>
}