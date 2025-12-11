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
    val nombre: String = "",
    val apellidos: String = "",
    val user: FirebaseUser? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

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

    // -----------------------------
    // CAMBIO DE CAMPOS
    // -----------------------------
    fun onNombreChange(new: String) {
        state = state.copy(nombre = new)
        nombreError = if (new.isBlank()) "Introduce tu nombre" else null
    }

    fun onApellidosChange(new: String) {
        state = state.copy(apellidos = new)
        apellidosError = if (new.isBlank()) "Introduce tus apellidos" else null
    }

    fun onEmailChange(new: String) {
        state = state.copy(email = new)
        emailError = Validator.validateEmail(new)
    }

    fun onPasswordChange(new: String) {
        state = state.copy(password = new)
        passwordError = Validator.validatePassword(new)
    }

    // -----------------------------
    // VALIDACIÓN LOGIN
    // -----------------------------
    private fun validateLogin(): Boolean {
        emailError = Validator.validateEmail(state.email)
        passwordError = Validator.validatePassword(state.password)

        return emailError == null && passwordError == null
    }

    // -----------------------------
    // VALIDACIÓN REGISTRO
    // -----------------------------
    private fun validateRegister(): Boolean {
        emailError = Validator.validateEmail(state.email)
        passwordError = Validator.validatePassword(state.password)
        nombreError = if (state.nombre.isBlank()) "Introduce tu nombre" else null
        apellidosError = if (state.apellidos.isBlank()) "Introduce tus apellidos" else null

        return emailError == null &&
                passwordError == null &&
                nombreError == null &&
                apellidosError == null
    }

    // -----------------------------
    // LOGIN
    // -----------------------------
    fun login(onSuccess: () -> Unit) {
        if (!validateLogin()) return

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

    // -----------------------------
    // REGISTRO
    // -----------------------------
    fun register(onSuccess: () -> Unit) {
        if (!validateRegister()) return

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

                val saveResult = UserRepository().saveUser(userData)

                if (saveResult.isSuccess) {
                    onSuccess()
                } else {
                    state = state.copy(error = saveResult.exceptionOrNull()?.message)
                }
            }
        }
    }
}