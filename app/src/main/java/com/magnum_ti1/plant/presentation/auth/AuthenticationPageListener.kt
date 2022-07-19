package com.magnum_ti1.plant.presentation.auth

import com.magnum_ti1.plant.data.entity.User

interface AuthenticationPageListener {
    fun onLoginPage()
    fun onRegisterPage()
    fun onAuthenticationSuccess(user: User)
}