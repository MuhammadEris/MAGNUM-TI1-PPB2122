package com.magnum_ti1.plant.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCaseImpl(private val authRepository: AuthRepository) : AuthUseCase {
    override suspend fun register(user: User): Flow<Response<User>> = authRepository.register(user)

    override suspend fun login(user: User): Flow<Response<User>> = authRepository.login(user)

    override suspend fun resetPassword(user: User): Flow<Response<Boolean>> =
        authRepository.resetPassword(user)

    override suspend fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount): Flow<Response<User>> =
        authRepository.signInWithGoogle(googleSignInAccount)
}