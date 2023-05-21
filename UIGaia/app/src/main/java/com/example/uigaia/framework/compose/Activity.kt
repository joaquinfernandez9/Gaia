package com.example.uigaia.framework.compose

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import com.example.uigaia.ui.theme.UIGaiaTheme
import androidx.navigation.compose.composable
import com.example.uigaia.framework.compose.login.Login

@AndroidEntryPoint
class Activity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val navController = rememberNavController()
            UIGaiaTheme {
                Navigation(navHostController = navController)

            }
        }
    }
}

@Composable
fun Navigation(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = "login"){
        composable("login"){
            Login()
        }
//        composable("register"){
//            Register()
//        }
//        composable("home"){
//            Home()
//        }
    }
}