package com.magnum_ti1.plant.domain.plant

import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.Plant
import com.magnum_ti1.plant.data.entity.PlantDetail
import com.magnum_ti1.plant.data.repository.plant.PlantRepository

class PlantUseCaseImpl(private val plantRepository: PlantRepository) : PlantUseCase {
    override suspend fun getPlantList(): Response<List<Plant>> = plantRepository.getPlantList()
    override suspend fun getDetailPlantList(plantId: Int?): Response<List<PlantDetail>> =
        plantRepository.getDetailPlantList(plantId)
}