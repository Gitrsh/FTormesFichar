package com.rsh.f_tormes.ui.screens.control

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.rsh.f_tormes.ui.components.EditarFichajeDialog
import com.rsh.f_tormes.ui.viewmodel.ControlHorasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlHorasScreen(
    navController: NavHostController,
    viewModel: ControlHorasViewModel = viewModel()
) {
    val loading by viewModel.loading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val fichajes by viewModel.fichajes.collectAsState()
    val editando by viewModel.fichajeEditando.collectAsState()
    val totalSemana by viewModel.totalSemana.collectAsState()
    val totalMes by viewModel.totalMes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Control de horas") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // Botones de fichaje
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { viewModel.ficharEntrada() }, enabled = !loading) {
                    Text("Entrada")
                }
                Button(onClick = { viewModel.ficharSalida() }, enabled = !loading) {
                    Text("Salida")
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Horas esta semana: ${String.format("%.2f", totalSemana)}h", modifier = Modifier.padding(8.dp))
            Text("Horas este mes: ${String.format("%.2f", totalMes)}h", modifier = Modifier.padding(8.dp))
            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(fichajes) { f ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                Modifier
                                    .weight(1f)
                                    .clickable { viewModel.abrirEditor(f) }
                            ) {
                                Text("Fecha: ${f.fecha}")
                                Text("Entrada: ${f.horaEntrada ?: "-"}")
                                Text("Salida: ${f.horaSalida ?: "-"}")
                                Text("Total: ${String.format("%.2f", f.totalHoras ?: 0.0)}h")
                            }
                            // Botón para borrar directamente desde la Card
                            IconButton(onClick = { viewModel.borrarFichaje(f) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Borrar fichaje"
                                )
                            }
                        }
                    }
                }
            }

            // Diálogo de edición
            if (editando != null) {
                EditarFichajeDialog(
                    fichaje = editando!!,
                    onDismiss = viewModel::cerrarEditor,
                    onFecha = { viewModel.actualizarCampo(fecha = it) },
                    onEntrada = { viewModel.actualizarCampo(entrada = it) },
                    onSalida = { viewModel.actualizarCampo(salida = it) },
                    onGuardar = { viewModel.guardarCambios() },
                    onBorrar = {
                        viewModel.borrarFichaje(editando!!)
                        viewModel.cerrarEditor()
                    }
                )
            }

            // Mensaje de estado
            if (mensaje != null) {
                AlertDialog(
                    onDismissRequest = viewModel::limpiarMensaje,
                    confirmButton = {
                        Button(onClick = viewModel::limpiarMensaje) { Text("OK") }
                    },
                    text = { Text(mensaje!!) }
                )
            }
        }
    }
}
