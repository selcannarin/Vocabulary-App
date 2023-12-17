package com.example.vocabularyapp.data.repository

import com.example.vocabularyapp.data.datasource.AuthDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {

    override suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): FirebaseUser? {
        return dataSource.register(
            fullName,
            email,
            password
        )
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        return dataSource.signInWithEmailPassword(email, password)
    }

    override suspend fun saveUser(fullName: String, email: String): Boolean {
        return dataSource.saveUser(fullName, email)
    }

    override fun signOut(): FirebaseUser? {
        return dataSource.signOut()
    }

    override fun getUser(): FirebaseUser? {
        return dataSource.getUser()
    }

    override suspend fun sendPasswordReset(email: String): Boolean {
        return dataSource.sendPasswordReset(email)
    }
}