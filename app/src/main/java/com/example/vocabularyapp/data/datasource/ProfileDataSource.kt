package com.example.vocabularyapp.data.datasource

import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.utils.UiState

interface ProfileDataSource {

    suspend fun getUserData(userEmail: String, result: (UiState<User>) -> Unit)

    suspend fun editUserData(user: User, result: (UiState<Boolean>) -> Unit)
}
