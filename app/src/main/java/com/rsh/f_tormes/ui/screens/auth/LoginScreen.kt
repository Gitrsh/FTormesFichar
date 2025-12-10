package com.rsh.f_tormes.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rsh.f_tormes.ui.auth.AuthViewModel
import com.rsh.f_tormes.ui.components.auth.AuthBackground
import com.rsh.f_tormes.ui.components.auth.AuthButton
import com.rsh.f_tormes.ui.components.auth.AuthEmailField
import com.rsh.f_tormes.ui.components.auth.AuthHeader
import com.rsh.f_tormes.ui.components.auth.AuthPasswordField


@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val state = viewModel.state

    AuthBackground {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(title = "Iniciar sesión")

            AuthEmailField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                error = viewModel.emailError
            )

            AuthPasswordField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                error = viewModel.passwordError
            )

            AuthButton(
                text = "Entrar",
                loading = state.loading,
                enabled = viewModel.emailError == null &&
                        viewModel.passwordError == null &&
                        state.email.isNotEmpty() &&
                        state.password.isNotEmpty()
            ) {
                viewModel.login(onLoginSuccess)
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onRegisterClick) {
                Text("¿Aún no estás registrado?", color = Color.White)
            }

            state.error?.let {
                Text(it, color = Color.Red)
            }
        }
    }
}
