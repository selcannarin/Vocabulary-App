package com.example.vocabularyapp.data.repository

import com.example.vocabularyapp.data.datasource.WordsDataSource
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.utils.UiState
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val wordsDataSource: WordsDataSource
) : WordsRepository {

    override suspend fun getRandomWordFromFirestore(): UiState<Word?> {
        return wordsDataSource.getRandomWordFromFirestore()
    }

    override suspend fun getFavoriteWords(result: (UiState<List<Word>>) -> Unit) {
        return wordsDataSource.getFavoriteWords(result)
    }

    override suspend fun addToFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.addToFavorite(wordId, result)
    }

    override suspend fun removeFromFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.removeFromFavorite(wordId, result)
    }

    override suspend fun isFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.isFavorite(wordId, result)
    }

    override suspend fun addTrueScore(result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.addTrueScore(result)
    }

    override suspend fun addFalseScore(result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.addFalseScore(result)
    }

    override suspend fun addLearnedScore(result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.addLearnedScore(result)
    }

    override suspend fun getLearnedWords(result: (UiState<List<Word>>) -> Unit) {
        return wordsDataSource.getLearnedWords(result)
    }

    override suspend fun addToLearnedWords(wordId: String, result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.addToLearnedWords(wordId, result)
    }

}