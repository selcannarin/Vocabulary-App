package com.example.vocabularyapp.data.repository

import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.utils.UiState

interface ProfileRepository {

    suspend fun getUserData(userEmail: String, result: (UiState<User>) -> Unit)

    suspend fun editUserData(user: User, result: (UiState<Boolean>) -> Unit)

}