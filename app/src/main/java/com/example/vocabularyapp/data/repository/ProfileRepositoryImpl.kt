package com.example.vocabularyapp.data.repository

import com.example.vocabularyapp.data.datasource.ProfileDataSource
import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.utils.UiState
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {

    override suspend fun getUserData(userEmail: String, result: (UiState<User>) -> Unit) {
        return profileDataSource.getUserData(userEmail, result)
    }

    override suspend fun editUserData(user: User, result: (UiState<Boolean>) -> Unit) {
        return profileDataSource.editUserData(user, result)
    }
}