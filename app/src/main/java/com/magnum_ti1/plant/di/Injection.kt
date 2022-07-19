package com.magnum_ti1.plant.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.magnum_ti1.plant.data.repository.auth.AuthRepository
import com.magnum_ti1.plant.data.repository.auth.AuthRepositoryImpl
import com.magnum_ti1.plant.data.repository.plant.PlantRepository
import com.magnum_ti1.plant.data.repository.plant.PlantRepositoryImpl
import com.magnum_ti1.plant.data.route.FirebaseReferences
import com.magnum_ti1.plant.domain.auth.AuthUseCase
import com.magnum_ti1.plant.domain.auth.AuthUseCaseImpl
import com.magnum_ti1.plant.domain.plant.PlantUseCase
import com.magnum_ti1.plant.domain.plant.PlantUseCaseImpl
import com.magnum_ti1.plant.presentation.auth.AuthenticationViewModel
import com.magnum_ti1.plant.presentation.content.detail.DetailViewModel
import com.magnum_ti1.plant.presentation.content.home.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val app: List<Module>
    get() = listOf(viewModelModule, repoModule, useCaseModule)

val viewModelModule = module {
    viewModel { AuthenticationViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}

val repoModule = module {
    single { Firebase.auth }
    single { Firebase.database }
    single { FirebaseReferences() }

    factory { AuthRepositoryImpl() } bind AuthRepository::class
    factory { PlantRepositoryImpl() } bind PlantRepository::class
}

val useCaseModule = module {
    factory { AuthUseCaseImpl(get()) } bind AuthUseCase::class
    factory { PlantUseCaseImpl(get()) } bind PlantUseCase::class
}