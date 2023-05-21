package com.example.uigaiav2.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uigaiav2.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding

    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=  FragmentRegisterBinding.inflate(inflater, container, false)


        // TODO: Add click listener to the Register button

        return view
    }
}
