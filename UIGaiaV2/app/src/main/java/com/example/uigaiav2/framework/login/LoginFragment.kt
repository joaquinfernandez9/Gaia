package com.example.uigaiav2.framework.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.uigaiav2.R
import com.example.uigaiav2.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentLoginBinding
import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.domain.model.dto.AccountDTO
import dagger.hilt.android.AndroidEntryPoint
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

        readTxt()
        setOnClicks()

        return binding.root
    }

    private fun readTxt() {
        with(binding){
            loading.visibility = View.VISIBLE
            val file = File(requireContext().filesDir, "secret.txt")
            if (file.exists()) {
                val fis = FileInputStream(file)
                val bytes = cryptoManager.decrypt(inputStream = fis)
                val response = bytes.decodeToString()
                val list = response.split(" ")
                viewModel.handleEvent(LoginEvent.Login(AccountDTO(list[0], list[1])))
                if (viewModel.state.value.boolean){
                    Toast.makeText(requireContext(), "Welcome back ${list[0]}", Toast.LENGTH_SHORT).show()
                    loading.visibility = View.GONE
                    findNavController().navigate(R.id.action_navigation_login_to_navigation_tree)
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
                val acc = AccountDTO(
                    username = nameText.text.toString(),
                    password = passText.text.toString()
                )

                if (acc.username == "" || acc.password == ""){
                    Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                } else{
                    viewModel.handleEvent(LoginEvent.Login(acc))

                    if (viewModel.state.value.boolean){
                        if(rememberMe.isChecked){
                            //encrypt to txt
                            val messageToEncrypt = nameText.text.toString() + " " + passText.text.toString()
                            val bytes = messageToEncrypt.toByteArray()
                            val file = File(requireContext().filesDir, "secret.txt")
                            if (!file.exists()) {
                                file.createNewFile()
                            }
                            val fos = FileOutputStream(file)
                            cryptoManager.encrypt(bytes = bytes, outputStream = fos).decodeToString()
                        }
                        findNavController().navigate(R.id.action_navigation_login_to_navigation_tree)
                    } else {
                        Toast.makeText(requireContext(), "Wrong username or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}