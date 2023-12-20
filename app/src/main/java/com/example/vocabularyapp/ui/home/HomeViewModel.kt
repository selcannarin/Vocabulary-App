package com.example.vocabularyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.data.repository.WordsRepository
import com.example.vocabularyapp.utils.AuthEvents
import com.example.vocabularyapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val wordsRepository: WordsRepository) :
    ViewModel() {

    private val _addToFavorite = MutableLiveData<UiState<Boolean>>()
    val addToFavorite: LiveData<UiState<Boolean>>
        get() = _addToFavorite

    private val _randomWord = MutableLiveData<Word?>()
    val randomWord: LiveData<Word?>
        get() = _randomWord

    private val _removeFromFavorite = MutableLiveData<UiState<Boolean>>()
    val removeFromFavorite: LiveData<UiState<Boolean>>
        get() = _removeFromFavorite

    fun addToFavorite(wordId: String) = viewModelScope.launch {
        _addToFavorite.value = UiState.Loading
        wordsRepository.addToFavorite(wordId) { _addToFavorite.value = it }
    }

    fun getRandomeWordFromFirestore() = viewModelScope.launch {
        wordsRepository.getRandomWordFromFirestore().let {
            if (it is UiState.Success) {
                _randomWord.value = it.data
            }
        }
    }

    fun removeFromFavorite(wordId: String) = viewModelScope.launch {
        _removeFromFavorite.value = UiState.Loading
        wordsRepository.removeFromFavorite(wordId) { _removeFromFavorite.value = it }
    }

}