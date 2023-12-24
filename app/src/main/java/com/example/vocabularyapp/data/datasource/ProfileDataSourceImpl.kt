package com.example.vocabularyapp.data.datasource

import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore
) : ProfileDataSource {

    override suspend fun getUserData(userEmail: String, result: (UiState<User>) -> Unit) {
        try {
            val userDocRef = firestore.collection("users").document(userEmail)
            val userData = userDocRef.get().await().toObject(User::class.java)
            if (userData != null) {
                result(UiState.Success(userData))
            } else {
                result(UiState.Failure("User data is null"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Failed to get user data: ${e.message}"))
        }
    }

    override suspend fun editUserData(user: User, result: (UiState<Boolean>) -> Unit) {

        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        try {
            val userDocRef = firestore.collection("users").document(currentUserEmail)
            userDocRef.set(user).addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener { e ->
                result(UiState.Failure("Failed to update user data: ${e.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Failed to update user data: ${e.message}"))
        }
    }


}
