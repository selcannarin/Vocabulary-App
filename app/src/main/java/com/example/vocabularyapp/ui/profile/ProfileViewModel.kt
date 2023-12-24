package com.example.vocabularyapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.data.repository.ProfileRepository
import com.example.vocabularyapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _getUserData = MutableLiveData<UiState<User>>()
    val getUserData: LiveData<UiState<User>>
        get() = _getUserData

    private val _editUserData = MutableLiveData<UiState<Boolean>>()
    val editUserData: LiveData<UiState<Boolean>>
        get() = _editUserData


    fun getUserData(userEmail: String) = viewModelScope.launch {
        _getUserData.value = UiState.Loading
        profileRepository.getUserData(userEmail) { _getUserData.value = it }
    }

    fun editUserData(user: User) = viewModelScope.launch {
        _editUserData.value = UiState.Loading
        profileRepository.editUserData(user) { _editUserData.value = it }
    }
}