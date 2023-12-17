package com.example.vocabularyapp.data.datasource

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthDataSource {

    override suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                saveUser(fullName, email)
            }

            firebaseUser
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthWeakPasswordException -> throw Exception("Zayıf şifre, daha güçlü bir şifre seçin.")
                is FirebaseAuthInvalidCredentialsException -> throw Exception("Geçersiz kimlik bilgileri.")
                is FirebaseAuthUserCollisionException -> throw Exception("Bu e-posta adresiyle zaten kayıtlı bir hesap var.")
                else -> throw Exception("Kayıt başarısız oldu: ${e.message}")
            }
        }
    }


    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException ->
                    throw Exception("Geçersiz e-posta veya şifre.")

                else -> throw Exception("Oturum açma başarısız oldu: ${e.message}")
            }
        }
    }

    override suspend fun saveUser(
        fullName: String,
        email: String
    ): Boolean {
        return try {
            val db = firestore
            val user = hashMapOf(
                "fullName" to fullName
            )

            db.collection("users")
                .document(email)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun signOut(): FirebaseUser? {
        firebaseAuth.signOut()
        return firebaseAuth.currentUser
    }

    override fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }


    override suspend fun sendPasswordReset(email: String): Boolean {
        if (!isEmailValid(email)) {
            throw IllegalArgumentException("Geçersiz e-posta formatı")
        }
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (e: FirebaseAuthInvalidUserException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}