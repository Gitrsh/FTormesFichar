package com.rsh.f_tormes.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rsh.f_tormes.model.Fichaje
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class ControlHorasRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val fichajesRef = db.collection("fichajes")
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    suspend fun ficharEntrada(): Result<Unit> {
        return try {
            val now = Date()

            val fichaje = Fichaje(
                id = "", // Firestore lo generará
                uid = uid!!,
                fecha = dateFormat.format(now),
                horaEntrada = timeFormat.format(now)
            )

            fichajesRef.add(fichaje).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun ficharSalida(): Result<Unit> {
        return try {
            val now = Date()
            val today = dateFormat.format(now)
            val salida = timeFormat.format(now)

            // Buscar fichaje de entrada sin salida
            val query = fichajesRef
                .whereEqualTo("uid", uid)
                .whereEqualTo("fecha", today)
                .whereEqualTo("horaSalida", null)
                .get()
                .await()

            if (query.documents.isEmpty()) {
                return Result.failure(Exception("No hay ficha de entrada registrada"))
            }

            val doc = query.documents.first()
            val entrada = doc.getString("horaEntrada")!!

            val total = calcularHoras(entrada, salida)

            // Actualizar documento
            fichajesRef.document(doc.id).update(
                mapOf(
                    "horaSalida" to salida,
                    "totalHoras" to total
                )
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerFichajes(): Result<List<Fichaje>> {
        return try {
            val query = fichajesRef
                .whereEqualTo("uid", uid)
                .get()
                .await()

            val lista = query.documents.map { doc ->
                Fichaje(
                    id = doc.id,
                    uid = doc.getString("uid") ?: "",
                    fecha = doc.getString("fecha") ?: "",
                    horaEntrada = doc.getString("horaEntrada"),
                    horaSalida = doc.getString("horaSalida"),
                    totalHoras = doc.getDouble("totalHoras")
                )
            }

            Result.success(lista)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarFichaje(fichaje: Fichaje): Result<Unit> {
        return try {
            val total = if (fichaje.horaEntrada != null && fichaje.horaSalida != null) {
                calcularHoras(fichaje.horaEntrada, fichaje.horaSalida)
            } else null

            fichajesRef.document(fichaje.id).set(
                fichaje.copy(totalHoras = total)
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun borrarFichaje(id: String): Result<Unit> {
        return try {
            fichajesRef.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calcularHoras(inicio: String, fin: String): Double {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val start = sdf.parse(inicio)
        val end = sdf.parse(fin)
        val diff = end.time - start.time
        return diff / (1000.0 * 60.0 * 60.0)
    }
}