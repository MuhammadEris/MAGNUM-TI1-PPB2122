package com.magnum_ti1.plant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.magnum_ti1.plant.common.launchActivity
import com.magnum_ti1.plant.presentation.content.home.MainActivity
import com.magnum_ti1.plant.presentation.auth.AuthenticationActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            launchActivity<MainActivity>()
        } else {
            launchActivity<AuthenticationActivity>()
        }

        finish()
    }
}