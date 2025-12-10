package com.rsh.f_tormes.ui.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.rsh.f_tormes.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextOverflow



// Componentes reutilizables  para autenticación

// FONDO GENERAL
// ---------------------------------------------------------------------
@Composable
fun AuthBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFBCE4A9), Color(0xFF568D4B))
                )
            ),
        content = content
    )
}

// ---------------------------------------------------------------------
// TÍTULO
// ---------------------------------------------------------------------
@Composable
fun AuthHeader(title: String, subtitle: String? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        subtitle?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ---------------------------------------------------------------------
// EMAIL FIELD
// ---------------------------------------------------------------------
@Composable
fun AuthEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
        )

        if (error != null)
            Text(error, color = Color.Red, fontSize = 12.sp)
    }

    Spacer(modifier = Modifier.height(16.dp))
}

// ---------------------------------------------------------------------
// PASSWORD FIELD
// ---------------------------------------------------------------------
@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    var visible by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (visible) "Ocultar" else "Mostrar"
                Text(icon, color = Color.Gray, modifier = Modifier.padding(4.dp))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
        )

        if (error != null)
            Text(error, color = Color.Red, fontSize = 12.sp)
    }

    Spacer(modifier = Modifier.height(16.dp))
}

// ---------------------------------------------------------------------
// BOTÓN REUTILIZABLE
// ---------------------------------------------------------------------
@Composable
fun AuthButton(
    text: String,
    loading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFA4D57E), Color(0xFF568D4B))
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (loading) CircularProgressIndicator(color = Color.White)
            else Text(text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}