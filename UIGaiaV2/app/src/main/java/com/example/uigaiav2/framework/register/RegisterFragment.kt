package com.example.uigaiav2.framework.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.uigaiav2.R
import com.example.uigaiav2.databinding.FragmentRegisterBinding
import com.example.uigaiav2.domain.model.Account
import dagger.hilt.android.AndroidEntryPoint

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
        //TODO: deactivate bottom navigation
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
                //TODO: check if passwords match and email is valid

                viewModel.handleEvent(RegisterEvent.Register(account))

                if (viewModel.state.value.boolean) {
                    etUsername.text?.clear()
                    etPassword.text?.clear()
                    etConfirmPassword.text?.clear()
                    etEmail.text?.clear()

                    //move to login fragment
                    findNavController().navigate(R.id.navigation_login)

                    Toast.makeText(context, "Account created, check your email.", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(context, "Error creating account.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
