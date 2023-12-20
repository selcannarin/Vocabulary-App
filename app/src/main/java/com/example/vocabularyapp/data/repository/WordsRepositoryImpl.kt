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

    override suspend fun getFavoriteWords(): List<Word> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.addToFavorite(wordId, result)
    }

    override suspend fun removeFromFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        return wordsDataSource.removeFromFavorite(wordId, result)
    }

    override suspend fun getTrueScore(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun addTrueScore(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFalseScore(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun addFalseScore(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getLearnedScore(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun addLearnedScore(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getLearnedWords(): List<Word> {
        TODO("Not yet implemented")
    }
}