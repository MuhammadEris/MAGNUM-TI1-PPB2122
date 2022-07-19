package com.magnum_ti1.plant.presentation.content.profile

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.magnum_ti1.plant.LauncherActivity
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.common.customToolbar
import com.magnum_ti1.plant.common.launchActivity
import com.magnum_ti1.plant.common.loadCircleImageUrl
import com.magnum_ti1.plant.data.local.db.DatabaseHelper
import com.magnum_ti1.plant.data.local.preferences.UserPreferences
import com.magnum_ti1.plant.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        dbHelper = DatabaseHelper(this)

        auth = Firebase.auth

        customToolbar(
            "",
            binding.toolbar.root,
            true
        )

        binding.apply {
            toolbar.tvLogout.setOnClickListener(this@ProfileActivity)
            imgEditProfile.setOnClickListener(this@ProfileActivity)
        }

        if (auth.currentUser == null) {
            launchActivity<LauncherActivity>()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val username = UserPreferences.initPreferences(this).userInfo.username
        val email = UserPreferences.initPreferences(this).userInfo.email
        val photo = UserPreferences.initPreferences(this).userInfo.photo
        binding.apply {
            tvUsernameProfile.text = username
            tvUserEmail.text = email
            if (photo.isNullOrBlank()) {
                imgProfile.setImageResource(R.drawable.sample_avatar)
            } else {
                imgProfile.loadCircleImageUrl(photo)
            }
        }

        val res = dbHelper.readData("1")
        val buffer = StringBuffer()
        while (res.moveToNext()) {
            buffer.append(res.getString(1))
        }
        if (buffer.toString().isNotEmpty()) {
            binding.tvBio.text = buffer
        } else {
            binding.tvBio.text = getString(R.string.bio_hint)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.toolbar.tvLogout.id -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.log_out))
                builder.setMessage(getString(R.string.logout_message))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    auth.signOut()
                    if (auth.currentUser == null) {
                        launchActivity<LauncherActivity>()
                        finish()
                    }
                    googleSignInClient.signOut().addOnCompleteListener(this) {}

                }
                builder.setNegativeButton(getString(R.string.no), null)
                builder.create().show()
            }
            binding.imgEditProfile.id -> launchActivity<EditProfileActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            launchActivity<LauncherActivity>()
            finish()
        }
    }
}