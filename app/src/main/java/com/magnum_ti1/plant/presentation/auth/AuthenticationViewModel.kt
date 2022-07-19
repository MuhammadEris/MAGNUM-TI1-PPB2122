package com.magnum_ti1.plant.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.domain.auth.AuthUseCase
import kotlinx.coroutines.Dispatchers

class AuthenticationViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    fun register(user: User) = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            authUseCase.register(user).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }

    fun login(user: User) = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            authUseCase.login(user).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }

    fun resetPassword(user: User) = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            authUseCase.resetPassword(user).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }

    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount) =
        liveData(Dispatchers.IO) {
            emit(Response.Loading())
            try {
                authUseCase.signInWithGoogle(googleSignInAccount).collect { emit(it) }
            } catch (e: Exception) {
                emit(Response.Fail(e))
            }
        }
}