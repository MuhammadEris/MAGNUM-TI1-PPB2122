package com.magnum_ti1.plant.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    suspend fun register(user: User): Flow<Response<User>>
    suspend fun login(user: User): Flow<Response<User>>
    suspend fun resetPassword(user: User): Flow<Response<Boolean>>
    suspend fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount): Flow<Response<User>>
}