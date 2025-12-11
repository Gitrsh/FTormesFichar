package com.rsh.f_tormes.ui.components

import com.rsh.f_tormes.model.Fichaje
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditarFichajeDialog(
    fichaje: Fichaje,
    onDismiss: () -> Unit,
    onFecha: (String) -> Unit,
    onEntrada: (String) -> Unit,
    onSalida: (String) -> Unit,
    onGuardar: () -> Unit,
    onBorrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar fichaje") },
        text = {
            Column {
                OutlinedTextField(
                    value = fichaje.fecha,
                    onValueChange = onFecha,
                    label = { Text("Fecha (yyyy-MM-dd)") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = fichaje.horaEntrada ?: "",
                    onValueChange = onEntrada,
                    label = { Text("Hora entrada (HH:mm)") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = fichaje.horaSalida ?: "",
                    onValueChange = onSalida,
                    label = { Text("Hora salida (HH:mm)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onGuardar) { Text("Guardar") }
        },
        dismissButton = {
            Row {
                Button(onClick = onBorrar) { Text("Borrar") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}