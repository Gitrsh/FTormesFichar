package com.rsh.f_tormes.model

data class Fichaje(
    val id: String = "",
    val uid: String = "",
    val fecha: String = "",
    val horaEntrada: String? = null,
    val horaSalida: String? = null,
    val totalHoras: Double? = null
)