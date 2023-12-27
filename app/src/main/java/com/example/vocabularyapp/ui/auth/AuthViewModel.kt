package com.example.vocabularyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyapp.data.repository.AuthRepository
import com.example.vocabularyapp.utils.AuthEvents
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean>
        get() = _registrationResult

    private val _signInResult = MutableLiveData<Boolean>()
    val signInResult: LiveData<Boolean>
        get() = _signInResult

    private val _passwordResetResult = MutableLiveData<Boolean>()
    val passwordResetResult: LiveData<Boolean>
        get() = _passwordResetResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val eventsChannel = Channel<AuthEvents>()
    val allEventsFlow = eventsChannel.receiveAsFlow()


    fun signOut() {
        _currentUser.value = null
        authRepository.signOut()
    }

    fun getUser() {
        _currentUser.value = authRepository.getUser()
    }

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.register(fullName, email, password)
                eventsChannel.send(AuthEvents.UserRegistered(user))
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun signInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.signInWithEmailPassword(email, password)
                eventsChannel.send(AuthEvents.UserSignedIn(user))
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            try {
                val result = authRepository.sendPasswordReset(email)
                eventsChannel.send(AuthEvents.PasswordResetSent(result))
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleException(exception: Exception) {
        viewModelScope.launch {
            val errorMessage = when (exception) {
                is FirebaseAuthInvalidUserException -> "Geçersiz kullanıcı hatası."
                else -> exception.localizedMessage ?: "Bir hata oluştu."
            }
            eventsChannel.send(AuthEvents.Error(errorMessage))
        }
    }
}

