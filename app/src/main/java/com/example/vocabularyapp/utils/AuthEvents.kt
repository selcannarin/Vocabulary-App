package com.example.vocabularyapp.utils

import com.google.firebase.auth.FirebaseUser

sealed class AuthEvents {
    data class UserRegistered(val user: FirebaseUser?) : AuthEvents()
    data class UserSignedIn(val user: FirebaseUser?) : AuthEvents()
    data class PasswordResetSent(val result: Boolean) : AuthEvents()
    data class Error(val errorMessage: String) : AuthEvents()
}
