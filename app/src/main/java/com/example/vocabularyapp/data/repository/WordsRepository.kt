package com.example.vocabularyapp.data.repository

import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.utils.UiState

interface WordsRepository {

    suspend fun getRandomWordFromFirestore(): UiState<Word?>

    suspend fun getFavoriteWords(result: (UiState<List<Word>>) -> Unit)

    suspend fun addToFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun removeFromFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun getTrueScore(result: (UiState<Int>) -> Unit)

    suspend fun addTrueScore(result: (UiState<Boolean>) -> Unit)

    suspend fun getFalseScore(result: (UiState<Int>) -> Unit)

    suspend fun addFalseScore(result: (UiState<Boolean>) -> Unit)

    suspend fun getLearnedScore(result: (UiState<Int>) -> Unit)

    suspend fun addLearnedScore(result: (UiState<Boolean>) -> Unit)

    suspend fun getLearnedWords(result: (UiState<List<Word>>) -> Unit)

    suspend fun addToLearnedWords(wordId: String, result: (UiState<Boolean>) -> Unit)
}