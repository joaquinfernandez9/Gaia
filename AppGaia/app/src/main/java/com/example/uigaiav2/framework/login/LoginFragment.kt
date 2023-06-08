package com.example.uigaiav2.framework.login

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
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
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentLoginBinding
import com.example.uigaiav2.domain.model.dto.AccountDTO
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding

    private val binding get() = _binding

    private val viewModel: LoginViewModel by viewModels()
    private val cryptoManager = CryptoManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        autoLogin()
        setOnClicks()

        return binding.root
    }

    private fun autoLogin() {
        with(binding) {
            loading.visibility = View.VISIBLE
            val file = File(requireContext().filesDir, "secret.txt")
            if (file.exists()) {
                val fis = FileInputStream(file)
                val bytes = cryptoManager.decrypt(inputStream = fis)
                val response = bytes.decodeToString()
                val list = response.split(" ")
                viewModel.handleEvent(LoginEvent.Login(AccountDTO(list[0], list[1])))
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.state.collect { state ->
                            if (state.error != null) {
                                Snackbar.make(
                                    requireView(),
                                    "Something went wrong",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.handleEvent(LoginEvent.ClearError)
                            } else {
                                Snackbar.make(
                                    requireView(),
                                    "Welcome back ${list[0]}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                loading.visibility = View.GONE
                                findNavController().navigate(R.id.action_navigation_login_to_navigation_tree)
                            }
                        }
                    }
                }
            }
            loading.visibility = View.GONE
        }

    }

    private fun setOnClicks() {
        with(binding) {

            //register
            registerButton.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_login_to_navigation_register)
            }

            //login
            loginButton.setOnClickListener {
                if (nameText.text.isNullOrBlank() || passText.text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val acc = AccountDTO(
                        username = nameText.text.toString(),
                        password = passText.text.toString()
                    )
                    viewModel.handleEvent(LoginEvent.Login(acc))
                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.state.collect { state ->
                                if (state.error!= null) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Wrong username or password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.handleEvent(LoginEvent.ClearError)
                                } else {
                                    if (rememberMe.isChecked) {
                                        with(context) {
                                            val messageToEncrypt =
                                                nameText.text.toString() + " " + passText.text.toString()
                                            val bytes = messageToEncrypt.toByteArray()
                                            val file = File(requireContext().filesDir, "secret.txt")
                                            if (!file.exists()) {
                                                file.createNewFile()
                                            }
                                            val fos = FileOutputStream(file)
                                            cryptoManager.encrypt(bytes = bytes, outputStream = fos)
                                                .decodeToString()
                                        }
                                    } else {
                                        val sharedPreferences = requireContext().getSharedPreferences(
                                            "sharedPrefs",
                                            MODE_PRIVATE
                                        )
                                        val editor = sharedPreferences.edit()
                                        editor.putString("username", nameText.text.toString())
                                        editor.apply()
                                    }
                                    findNavController().navigate(R.id.action_navigation_login_to_navigation_tree)
                                }
                            }
                        }
                    }


                }
            }
        }
    }

}