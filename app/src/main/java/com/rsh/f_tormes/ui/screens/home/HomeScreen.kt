package com.rsh.f_tormes.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.rsh.f_tormes.navigation.Screen
import com.rsh.f_tormes.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val user = viewModel.userData.collectAsState().value
    val loading = viewModel.loading.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (user != null) "Hola, ${user.nombre}" else "Inicio",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }) {
                        Text("Salir")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> CircularProgressIndicator()
                user != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Bienvenido de nuevo 👋")
                        Spacer(Modifier.height(20.dp))

                        // Menú de inicio con botones
                        Button(onClick = { navController.navigate(Screen.ControlHoras.route) }) {
                            Text("Control de horas")
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(onClick = { /* Aquí irá Mi perfil */ }) {
                            Text("Mi perfil")
                        }
                    }
                }
                else -> Text("No se pudieron cargar tus datos 😔")
            }
        }
    }
}