package com.rsh.f_tormes.auth.validation

import android.util.Patterns

object Validator {

    fun validateEmail(email: String): String? =
        when {
            email.isBlank() -> "El email no puede estar vacío"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Formato de email no válido"
            else -> null
        }

    fun validatePassword(password: String): String? =
        when {
            password.isBlank() -> "La contraseña no puede estar vacía"
            password.length < 6 -> "Debe tener al menos 6 caracteres"
            else -> null
        }
}