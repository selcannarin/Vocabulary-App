package com.example.vocabularyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.data.repository.WordsRepository
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

    private val _addTrueScore = MutableLiveData<UiState<Boolean>>()
    val addTrueScore: LiveData<UiState<Boolean>>
        get() = _addTrueScore

    private val _addFalseScore = MutableLiveData<UiState<Boolean>>()
    val addFalseScore: LiveData<UiState<Boolean>>
        get() = _addFalseScore

    private val _addLearnedWord = MutableLiveData<UiState<Boolean>>()
    val addLearnedWord: LiveData<UiState<Boolean>>
        get() = _addLearnedWord

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

    fun addTrueScore() = viewModelScope.launch {
        _addTrueScore.value = UiState.Loading
        wordsRepository.addTrueScore { _addTrueScore.value = it }
    }

    fun addFalseScore() = viewModelScope.launch {
        _addFalseScore.value = UiState.Loading
        wordsRepository.addFalseScore { _addFalseScore.value = it }
    }

    fun addLearnedWord(wordId: String) = viewModelScope.launch {
        _addLearnedWord.value = UiState.Loading
        wordsRepository.addToLearnedWords(wordId) { _addLearnedWord.value = it }
    }

}