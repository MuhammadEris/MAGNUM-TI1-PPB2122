package com.magnum_ti1.plant.data.repository.plant

import com.google.firebase.firestore.ktx.toObjects
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.Plant
import com.magnum_ti1.plant.data.entity.PlantDetail
import com.magnum_ti1.plant.data.route.FirebaseReferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlantRepositoryImpl : PlantRepository, KoinComponent {
    private val firebaseReferences: FirebaseReferences by inject()

    override suspend fun getPlantList(): Response<List<Plant>> = suspendCoroutine { continuation ->
        firebaseReferences.plantReferences().get().addOnSuccessListener {
            try {
                continuation.resume(Response.Success(it.toObjects()))
            } catch (e: Exception) {
                continuation.resume(Response.Fail(e))
            }
        }.addOnFailureListener {
            continuation.resume(Response.Fail(it))
        }
    }

    override suspend fun getDetailPlantList(plantId: Int?): Response<List<PlantDetail>> =
        suspendCoroutine { continuation ->
            firebaseReferences.plantReferences().whereEqualTo("id", plantId).get().addOnSuccessListener {
                try {
                    continuation.resume(Response.Success(it.toObjects()))
                } catch (e: Exception) {
                    continuation.resume(Response.Fail(e))
                }
            }.addOnFailureListener {
                continuation.resume(Response.Fail(it))
            }
        }
}