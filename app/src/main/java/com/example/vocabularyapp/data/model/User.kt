package com.example.vocabularyapp.data.model

data class User(
    val fullName: String = "",
    val email: String = "",
    val trueScore: Int = 0,
    val falseScore: Int = 0,
    val learnedScore: Int = 0,
    val learned: List<String> = listOf(),
    val favorites: List<String> = listOf()
)
