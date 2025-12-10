package com.rsh.f_tormes.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rsh.f_tormes.ui.auth.AuthViewModel
import com.rsh.f_tormes.ui.components.auth.AuthBackground
import com.rsh.f_tormes.ui.components.auth.AuthButton
import com.rsh.f_tormes.ui.components.auth.AuthEmailField
import com.rsh.f_tormes.ui.components.auth.AuthHeader
import com.rsh.f_tormes.ui.components.auth.AuthPasswordField


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit
) {
    val state = viewModel.state
    val focus = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    // Fondo degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFBCE4A9), Color(0xFF568D4B))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Registro",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 40.dp)
            )

            // -----------------------
            //      NOMBRE
            // -----------------------
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focus.moveFocus(FocusDirection.Down) }
                )
            )
            viewModel.nombreError?.let {
                Text(text = it, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -----------------------
            //      APELLIDOS
            // -----------------------
            OutlinedTextField(
                value = state.apellidos,
                onValueChange = { viewModel.onApellidosChange(it) },
                label = { Text("Apellidos") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focus.moveFocus(FocusDirection.Down) }
                )
            )
            viewModel.apellidosError?.let {
                Text(text = it, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -----------------------
            //      EMAIL
            // -----------------------
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp)),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focus.moveFocus(FocusDirection.Down) }
                )
            )
            viewModel.emailError?.let {
                Text(text = it, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -----------------------
            //      CONTRASEÑA
            // -----------------------
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp)),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focus.clearFocus()
                        keyboard?.hide()
                    }
                )
            )
            viewModel.passwordError?.let {
                Text(text = it, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -----------------------
            //      BOTÓN REGISTRO
            // -----------------------
            Button(
                onClick = {
                    focus.clearFocus()
                    keyboard?.hide()
                    viewModel.register(onRegisterSuccess)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFA4D57E),
                                    Color(0xFF568D4B)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.loading) "Registrando..." else "Registrarse",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Error general desde Firebase Auth
            state.error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }
        }
    }
}


