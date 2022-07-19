package com.magnum_ti1.plant.presentation.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magnum_ti1.plant.presentation.content.home.MainActivity
import com.magnum_ti1.plant.common.launchActivity
import com.magnum_ti1.plant.common.replaceFragment
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.data.local.preferences.UserPreferences
import com.magnum_ti1.plant.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity(), AuthenticationPageListener {
    private lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(binding.frameLayout.id, LoginFragment())
        }
    }

    override fun onLoginPage() = replaceFragment(binding.frameLayout.id, LoginFragment())

    override fun onRegisterPage() = replaceFragment(binding.frameLayout.id, RegisterFragment())
    override fun onAuthenticationSuccess(user: User) {
        UserPreferences.initPreferences(this).userInfo = user
        launchActivity<MainActivity>()
        finish()
    }
}