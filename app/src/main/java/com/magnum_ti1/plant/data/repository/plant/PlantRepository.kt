package com.magnum_ti1.plant.data.repository.plant

import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.Plant
import com.magnum_ti1.plant.data.entity.PlantDetail

interface PlantRepository {
    suspend fun getPlantList(): Response<List<Plant>>
    suspend fun getDetailPlantList(plantId: Int?): Response<List<PlantDetail>>
}