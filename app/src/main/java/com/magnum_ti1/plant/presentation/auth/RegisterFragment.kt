package com.magnum_ti1.plant.presentation.auth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.common.*
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private lateinit var pageListener: AuthenticationPageListener
    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            permission.entries.forEach {
                Log.d("DEBUG", "${it.key} = ${it.value}")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            tvLogin.setOnClickListener(this@RegisterFragment)
            btnCreateAccount.setOnClickListener(this@RegisterFragment)
        }

        startSmsPermissionRequest()
    }

    private fun startSmsPermissionRequest() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECEIVE_SMS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS
                )
            )
        }
    }

    private fun registerObserver(user: User) {
        authenticationViewModel.register(user).observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> binding?.loading?.root?.isVisible = true
                is Response.Success -> {
                    val obj = SmsManager.getDefault()
                    obj?.sendTextMessage(
                        binding?.edtPhoneNumber?.text.toString(),
                        null,
                        getString(R.string.successfully_message),
                        null,
                        null
                    )
                    pageListener.onAuthenticationSuccess(it.data)
                }
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
                tvLogin.id -> pageListener.onLoginPage()
                btnCreateAccount.id -> {
                    val username = edtUsername.convertToString()
                    val email = edtEmail.convertToString()
                    val password = edtPassword.convertToString()
                    val phoneNumber = edtPhoneNumber.convertToString()

                    val user = User(
                        username = username,
                        email = email,
                        password = password,
                        phoneNumber = phoneNumber
                    )

                    when {
                        username.isEmpty() -> edtUsername.error = getString(R.string.field_required)
                        email.invalidEmail() -> edtEmail.error = getString(R.string.invalid_email)
                        email.isEmpty() -> edtEmail.error = getString(R.string.field_required)
                        password.isEmpty() -> edtPassword.error = getString(R.string.field_required)
                        phoneNumber.isEmpty() -> edtPhoneNumber.error =
                            getString(R.string.field_required)
                        else -> {
                            registerObserver(user)
                            edtUsername.clearFocus()
                            edtEmail.clearFocus()
                            edtPassword.clearFocus()
                            edtPhoneNumber.clearFocus()
                        }
                    }
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