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

    private val _addLearnedScore = MutableLiveData<UiState<Boolean>>()
    val addLearnedScore: LiveData<UiState<Boolean>>
        get() = _addLearnedScore

    private val _addLearnedWord = MutableLiveData<UiState<Boolean>>()
    val addLearnedWord: LiveData<UiState<Boolean>>
        get() = _addLearnedWord

    private val _getFavorites = MutableLiveData<UiState<List<Word>>>()
    val getFavorites: LiveData<UiState<List<Word>>>
        get() = _getFavorites

    private val _getLearnedWords = MutableLiveData<UiState<List<Word>>>()
    val getLearnedWords: LiveData<UiState<List<Word>>>
        get() = _getLearnedWords

    private val _isFavorite = MutableLiveData<UiState<Boolean>>()
    val isFavorite: LiveData<UiState<Boolean>>
        get() = _isFavorite

    private val _getAllWords = MutableLiveData<UiState<List<Word>>>()
    val getAllWords: LiveData<UiState<List<Word>>>
        get() = _getAllWords

    private val _addWord = MutableLiveData<UiState<Boolean>>()
    val addWord: LiveData<UiState<Boolean>>
        get() = _addWord

    private val _deleteWord = MutableLiveData<UiState<Boolean>>()
    val deleteWord: LiveData<UiState<Boolean>>
        get() = _deleteWord

    private val _updateWord = MutableLiveData<UiState<Boolean>>()
    val updateWord: LiveData<UiState<Boolean>>
        get() = _updateWord

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

    fun addLearnedScore() = viewModelScope.launch {
        _addLearnedScore.value = UiState.Loading
        wordsRepository.addLearnedScore { _addLearnedScore.value = it }
    }

    fun addLearnedWord(wordId: String) = viewModelScope.launch {
        _addLearnedWord.value = UiState.Loading
        wordsRepository.addToLearnedWords(wordId) { _addLearnedWord.value = it }
    }

    fun getFavorites() = viewModelScope.launch {
        _getFavorites.value = UiState.Loading
        wordsRepository.getFavoriteWords { _getFavorites.value = it }
    }

    fun getLearnedWords() = viewModelScope.launch {
        _getLearnedWords.value = UiState.Loading
        wordsRepository.getLearnedWords { _getLearnedWords.value = it }
    }

    fun isFavorite(wordId: String) = viewModelScope.launch {
        _isFavorite.value = UiState.Loading
        wordsRepository.isFavorite(wordId) { _isFavorite.value = it }
    }

    fun getAllWords() = viewModelScope.launch {
        _getAllWords.value = UiState.Loading
        wordsRepository.getAllWords { _getAllWords.value = it }
    }

    fun addWord(word: Word) = viewModelScope.launch {
        _addWord.value = UiState.Loading
        wordsRepository.addWord(word) { _addWord.value = it }
    }

    fun deleteWord(wordId: String) = viewModelScope.launch {
        _deleteWord.value = UiState.Loading
        wordsRepository.deleteWord(wordId) { _deleteWord.value = it }
    }

    fun updateWord(word: Word) = viewModelScope.launch {
        _updateWord.value = UiState.Loading
        wordsRepository.updateWord(word) { _updateWord.value = it }
    }

}