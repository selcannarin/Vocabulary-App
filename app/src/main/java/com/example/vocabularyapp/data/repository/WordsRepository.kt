package com.example.vocabularyapp.data.repository

import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.utils.UiState

interface WordsRepository {

    suspend fun getRandomWordFromFirestore(): UiState<Word?>

    suspend fun getFavoriteWords(): List<Word>

    suspend fun addToFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun removeFromFavorite(wordId: String, result: (UiState<Boolean>) -> Unit)

    suspend fun getTrueScore(): Int

    suspend fun addTrueScore(): Boolean

    suspend fun getFalseScore(): Int

    suspend fun addFalseScore(): Boolean

    suspend fun getLearnedScore(): Int

    suspend fun addLearnedScore(): Boolean

    suspend fun getLearnedWords(): List<Word>
}