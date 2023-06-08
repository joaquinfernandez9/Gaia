package com.example.uigaiav2.framework.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.uigaiav2.R
import com.example.uigaiav2.databinding.FragmentRegisterBinding
import com.example.uigaiav2.domain.model.Account
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding

    private val binding get() = _binding

    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setOnClicks()
        return binding.root
    }

    private fun setOnClicks() {

        with(binding) {
            btnRegister.setOnClickListener {
                val account = Account(
                    username = etUsername.text.toString(),
                    password = etPassword.text.toString(),
                    email = etEmail.text.toString()
                )

                if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                    Snackbar.make(
                        root,
                        "Error, las contraseñas no coinciden.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
//                } else if (!etEmail.toString().endsWith("@gmail.com")) {
//                    Snackbar.make(
//                        root,
//                        "Error, el email no es válido.",
//                        Snackbar.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
                } else {
                    viewModel.handleEvent(RegisterEvent.Register(account))
                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.state.collect { value ->
                                if (!value.boolean) {
                                    value.error?.let {
                                        Snackbar.make(
                                            root,
                                            "Error, account can't be created.",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                                    viewModel.clearError()
                                } else {
                                    etUsername.text?.clear()
                                    etPassword.text?.clear()
                                    etConfirmPassword.text?.clear()
                                    etEmail.text?.clear()
                                    findNavController().navigate(R.id.navigation_login)
                                    Toast.makeText(
                                        context,
                                        "Account created, check your email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


