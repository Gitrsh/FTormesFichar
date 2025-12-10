package com.rsh.f_tormes.model

import com.google.firebase.Timestamp

data class UserData(
    val uid: String = "",
    val email: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val fechaRegistro: Timestamp = Timestamp.now()
)