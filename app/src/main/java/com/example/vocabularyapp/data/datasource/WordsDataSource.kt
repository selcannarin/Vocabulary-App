package com.example.vocabularyapp.data.datasource

import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.utils.UiState

interface WordsDataSource {

    suspend fun getRandomWordFromFirestore(): UiState<Word?>

    suspend fun getFavoriteWords(result: (UiState<List<Word>>) -> Unit)

    suspend fun addToFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun removeFromFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun isFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun addTrueScore(result: (UiState<Boolean>) -> Unit)

    suspend fun addFalseScore(result: (UiState<Boolean>) -> Unit)

    suspend fun addLearnedScore(result: (UiState<Boolean>) -> Unit)

    suspend fun getLearnedWords(result: (UiState<List<Word>>) -> Unit)

    suspend fun addToLearnedWords(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun getAllWords(result: (UiState<List<Word>>) -> Unit)

    suspend fun addWord(word: Word, result: (UiState<Boolean>) -> Unit)

    suspend fun deleteWord(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun updateWord(word: Word, result: (UiState<Boolean>) -> Unit)
}