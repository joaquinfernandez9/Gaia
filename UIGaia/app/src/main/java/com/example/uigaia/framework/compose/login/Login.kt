package com.example.uigaia.framework.compose.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun Login(onNavigate: (String) -> Unit = {}) {
    val viewModel: ViewModelLogin = hiltViewModel()
    val state by viewModel.state.collectAsState()

   Box(modifier = Modifier.fillMaxSize()){

       Column(modifier = Modifier.
            fillMaxWidth().padding(16.dp).align(Alignment.Center)) {

           Spacer(modifier = Modifier.height(16.dp))
           TextField(
               value = state.username,
               onValueChange = {},
               label = { Text("Nombre de usuario") }
           )
           Spacer(modifier = Modifier.height(16.dp))
           TextField(
               value = state.password,
               onValueChange = {},
               label = { Text("Contraseña") },
               visualTransformation = PasswordVisualTransformation()
           )
           Spacer(modifier = Modifier.height(16.dp))
           Button(
               onClick = {},
               modifier = Modifier.fillMaxWidth()
           ) {
               Text("Iniciar sesión")
           }

       }
   }
}