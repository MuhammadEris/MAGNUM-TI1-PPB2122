package com.magnum_ti1.plant.presentation.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.common.*
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.databinding.ActivityForgotPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customToolbar(
            "",
            binding.toolbar.root,
            true
        )

        binding.apply {
            btnRecoverPassword.setOnClickListener(this@ForgotPasswordActivity)
        }
    }

    private fun resetPasswordObserver(user: User) {
        authenticationViewModel.resetPassword(user).observe(this) {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> getString(
                    R.string.send_instruction_message,
                    user.email
                ).showMessage(this)
                is Response.Fail -> it.e.message?.showMessage(this)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnRecoverPassword.id -> {
                binding.apply {
                    val email = edtEmail.convertToString()
                    val user = User(
                        email = email
                    )

                    when {
                        email.invalidEmail() -> edtEmail.error = getString(R.string.invalid_email)
                        email.isEmpty() -> edtEmail.error = getString(R.string.field_required)
                        else -> resetPasswordObserver(user)
                    }
                }
            }
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
}