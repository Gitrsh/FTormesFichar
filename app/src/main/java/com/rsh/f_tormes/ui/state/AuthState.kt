package com.rsh.f_tormes.ui.state

import com.google.firebase.auth.FirebaseUser

data class AuthState(
    val email: String = "",
    val password: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val user: FirebaseUser? = null
)
sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}