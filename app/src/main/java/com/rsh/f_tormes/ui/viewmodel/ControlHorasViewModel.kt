package com.rsh.f_tormes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsh.f_tormes.data.ControlHorasRepository
import com.rsh.f_tormes.model.Fichaje
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ControlHorasViewModel(
    private val repo: ControlHorasRepository = ControlHorasRepository()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _fichajes = MutableStateFlow<List<Fichaje>>(emptyList())
    val fichajes: StateFlow<List<Fichaje>> = _fichajes

    private val _fichajeEditando = MutableStateFlow<Fichaje?>(null)
    val fichajeEditando: StateFlow<Fichaje?> = _fichajeEditando

    private val _totalSemana = MutableStateFlow(0.0)
    val totalSemana: StateFlow<Double> = _totalSemana

    private val _totalMes = MutableStateFlow(0.0)
    val totalMes: StateFlow<Double> = _totalMes

    init {
        cargarFichajes()
    }

    fun cargarFichajes() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.obtenerFichajes()
            _loading.value = false

            result.onSuccess { lista ->
                _fichajes.value = lista.sortedByDescending { it.fecha }
                calcularTotales()
            }.onFailure {
                _mensaje.value = it.message
            }
        }
    }

    private fun calcularTotales() {
        val hoy = Calendar.getInstance()
        val semanaInicio = hoy.clone() as Calendar
        semanaInicio.set(Calendar.DAY_OF_WEEK, semanaInicio.firstDayOfWeek)
        val mesInicio = hoy.clone() as Calendar
        mesInicio.set(Calendar.DAY_OF_MONTH, 1)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        _totalSemana.value = _fichajes.value
            .filter { it.totalHoras != null && sdf.parse(it.fecha) >= semanaInicio.time }
            .sumOf { it.totalHoras ?: 0.0 }

        _totalMes.value = _fichajes.value
            .filter { it.totalHoras != null && sdf.parse(it.fecha) >= mesInicio.time }
            .sumOf { it.totalHoras ?: 0.0 }
    }

    fun abrirEditor(f: Fichaje) {
        _fichajeEditando.value = f
    }

    fun cerrarEditor() {
        _fichajeEditando.value = null
    }

    fun actualizarCampo(fecha: String? = null, entrada: String? = null, salida: String? = null) {
        val actual = _fichajeEditando.value ?: return

        _fichajeEditando.value = actual.copy(
            fecha = fecha ?: actual.fecha,
            horaEntrada = entrada ?: actual.horaEntrada,
            horaSalida = salida ?: actual.horaSalida
        )
    }

    fun guardarCambios() {
        val f = _fichajeEditando.value ?: return
        viewModelScope.launch {
            _loading.value = true
            val result = repo.actualizarFichaje(f)
            _loading.value = false

            if (result.isSuccess) {
                _mensaje.value = "Fichaje actualizado"
                cerrarEditor()
                cargarFichajes()
            } else {
                _mensaje.value = result.exceptionOrNull()?.message ?: "Error al guardar"
            }
        }
    }

    fun ficharEntrada() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.ficharEntrada()
            _loading.value = false

            _mensaje.value = result.fold(
                onSuccess = { "¡Entrada registrada!" },
                onFailure = { it.message ?: "Error al fichar entrada" }
            )

            if (result.isSuccess) cargarFichajes()
        }
    }

    fun ficharSalida() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.ficharSalida()
            _loading.value = false

            _mensaje.value = result.fold(
                onSuccess = { "¡Salida registrada!" },
                onFailure = { it.message ?: "Error al fichar salida" }
            )

            if (result.isSuccess) cargarFichajes()
        }
    }

    fun borrarFichaje(f: Fichaje) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.borrarFichaje(f.id)
            _loading.value = false

            if (result.isSuccess) {
                _mensaje.value = "Fichaje borrado"
                cerrarEditor()
                cargarFichajes()
            } else {
                _mensaje.value = result.exceptionOrNull()?.message ?: "Error al borrar"
            }
        }
    }


    fun limpiarMensaje() {
        _mensaje.value = null
    }
}