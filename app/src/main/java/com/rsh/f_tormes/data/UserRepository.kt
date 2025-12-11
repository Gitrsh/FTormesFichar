package com.rsh.f_tormes.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.rsh.f_tormes.model.UserData
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val usersRef = db.collection("users")

    /** Guarda o actualiza un usuario en Firestore */
    suspend fun saveUser(user: UserData): Result<Unit> {
        return try {
            usersRef.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Con esta función obtenemos el usuario desde firestor
    suspend fun getUserById(uid: String): Result<UserData?> {
        return try {
            val snapshot = usersRef.document(uid).get().await()
            Result.success(snapshot.toObject(UserData::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}