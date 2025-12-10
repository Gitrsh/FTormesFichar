package com.rsh.f_tormes.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.rsh.f_tormes.auth.AuthRepository
import com.rsh.f_tormes.auth.validation.Validator
import com.rsh.f_tormes.data.UserRepository
import com.rsh.f_tormes.model.UserData
import kotlinx.coroutines.launch
import com.google.firebase.Timestamp
import com.rsh.f_tormes.ui.state.AuthState

data class AuthState(
    val email: String = "",
    val password: String = "",
    val user: FirebaseUser? = null,
    val loading: Boolean = false,
    val error: String? = null
)

/**
 * - Validaciones básicas (email y contraseña)
 * - Limpia de errores al cambiar valores
 * - resetState() para evitar compartir estado entre pantallas
 * - login/register usan viewModelScope y manejan resultados/excepciones
 */
class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    // Estado privado mutabe + getter público inmutable
    var state by mutableStateOf(AuthState())
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var nombreError by mutableStateOf<String?>(null)
        private set

    var apellidosError by mutableStateOf<String?>(null)
        private set

    fun onNombreChange(new: String) {
        state = state.copy(nombre = new)
        nombreError = if (new.isBlank()) "Introduce tu nombre" else null
    }

    fun onApellidosChange(new: String) {
        state = state.copy(apellidos = new)
        apellidosError = if (new.isBlank()) "Introduce tus apellidos" else null
    }

    // Cambia email y limpia error previa si existe
    fun onEmailChange(new: String) {
        state = state.copy(email = new)
        emailError = Validator.validateEmail(new)
    }

    // Cambia password y limpia error previa si existe
    fun onPasswordChange(new: String) {
        state = state.copy(password = new)
        passwordError = Validator.validatePassword(new)
    }

    private fun validateAll(): Boolean {
        emailError = Validator.validateEmail(state.email)
        passwordError = Validator.validatePassword(state.password)
        nombreError = if (state.nombre.isBlank()) "Introduce tu nombre" else null
        apellidosError = if (state.apellidos.isBlank()) "Introduce tus apellidos" else null

        return emailError == null &&
                passwordError == null &&
                nombreError == null &&
                apellidosError == null
    }

    fun login(onSuccess: () -> Unit) {
        if (!validateAll()) return

        viewModelScope.launch {
            state = state.copy(loading = true, error = null)
            val result = repo.login(state.email, state.password)

            state = if (result.isSuccess) {
                state.copy(user = result.getOrNull(), loading = false)
            } else {
                state.copy(error = result.exceptionOrNull()?.message, loading = false)
            }

            if (state.user != null) onSuccess()
        }
    }


     // Intenta registrar. onSuccess se ejecuta si hay usuario.

    fun register(onSuccess: () -> Unit) {
        if (!validateAll()) return

        viewModelScope.launch {
            state = state.copy(loading = true, error = null)
            val result = repo.register(state.email, state.password)

            state = if (result.isSuccess) {
                state.copy(user = result.getOrNull(), loading = false)
            } else {
                state.copy(error = result.exceptionOrNull()?.message, loading = false)
            }

            if (state.user != null) {

                val userData = UserData(
                    uid = state.user!!.uid,
                    email = state.email,
                    nombre = state.nombre,
                    apellidos = state.apellidos,
                    fechaRegistro = Timestamp.now()
                )

                UserRepository().saveUser(userData)

                onSuccess()
            }
        }
    }
}
