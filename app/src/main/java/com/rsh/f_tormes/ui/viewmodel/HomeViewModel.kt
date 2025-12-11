package com.rsh.f_tormes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsh.f_tormes.data.UserRepository
import com.rsh.f_tormes.model.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val uid = auth.currentUser?.uid ?: return
        _loading.value = true

        viewModelScope.launch {
            val result = userRepository.getUserById(uid)
            _userData.value = result.getOrNull()
            _loading.value = false
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        onLoggedOut()
    }
}