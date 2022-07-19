package com.magnum_ti1.plant.presentation.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.common.*
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private lateinit var pageListener: AuthenticationPageListener
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    signInWithGoogleObserver(account)
                } catch (e: Exception) {
                    e.message?.showMessage(requireContext())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding?.apply {
            tvRegisterNow.setOnClickListener(this@LoginFragment)
            btnLogin.setOnClickListener(this@LoginFragment)
            tvForgotPassword.setOnClickListener(this@LoginFragment)
            btnLoginWithGoogle.setOnClickListener(this@LoginFragment)
        }
    }

    private fun loginObserver(user: User) {
        authenticationViewModel.login(user).observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> binding?.loading?.root?.isVisible = true
                is Response.Success -> pageListener.onAuthenticationSuccess(it.data)
                is Response.Fail -> {
                    binding?.loading?.root?.isVisible = false
                    it.e.message?.showMessage(requireContext())
                }
            }
        }
    }

    private fun signInWithGoogleObserver(googleSignInAccount: GoogleSignInAccount) {
        authenticationViewModel.signInWithGoogle(googleSignInAccount)
            .observe(viewLifecycleOwner) {
                when (it) {
                    is Response.Loading -> binding?.loading?.root?.isVisible = true
                    is Response.Success -> pageListener.onAuthenticationSuccess(it.data)
                    is Response.Fail -> {
                        binding?.loading?.root?.isVisible = false
                        it.e.message?.showMessage(requireContext())
                    }
                }
            }
    }

    override fun onClick(p0: View?) {
        binding?.apply {
            when (p0?.id) {
                tvRegisterNow.id -> pageListener.onRegisterPage()
                btnLogin.id -> {
                    val email = edtEmail.convertToString()
                    val password = edtPassword.convertToString()
                    val user = User(
                        email = email,
                        password = password
                    )
                    when {
                        email.invalidEmail() -> edtEmail.error = getString(R.string.invalid_email)
                        email.isEmpty() -> edtEmail.error = getString(R.string.field_required)
                        password.isEmpty() -> edtPassword.error = getString(R.string.field_required)
                        else -> {
                            loginObserver(user)
                            edtEmail.clearFocus()
                            edtPassword.clearFocus()
                        }
                    }
                }
                tvForgotPassword.id -> requireContext().launchActivity<ForgotPasswordActivity>()
                btnLoginWithGoogle.id -> {
                    val signInIntent = googleSignInClient.signInIntent
                    resultLauncher.launch(signInIntent)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pageListener = context as AuthenticationPageListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}