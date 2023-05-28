package com.example.uigaiav2.framework

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uigaiav2.R
import com.example.uigaiav2.crypt.CryptoManager
import com.example.uigaiav2.databinding.ActivityMainBinding
import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.framework.login.LoginEvent
import com.example.uigaiav2.framework.login.LoginViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.navigation_login -> navView.visibility = View.GONE
                R.id.navigation_register -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
        }

        navView.setupWithNavController(navController)
    }


}