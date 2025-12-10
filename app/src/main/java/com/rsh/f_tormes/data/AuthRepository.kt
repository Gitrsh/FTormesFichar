package com.rsh.f_tormes.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser?> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser(): FirebaseUser? = auth.currentUser
}