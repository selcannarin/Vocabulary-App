package com.example.vocabularyapp.data.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): FirebaseUser?

    suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser?

    suspend fun saveUser(fullName: String, email: String): Boolean

    fun signOut(): FirebaseUser?

    fun getUser(): FirebaseUser?

    suspend fun sendPasswordReset(email: String): Boolean


}