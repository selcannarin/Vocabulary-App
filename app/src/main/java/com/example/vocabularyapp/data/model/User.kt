package com.example.vocabularyapp.data.model

data class User(
    val name: String,
    val surname: String,
    val email: String,
    val trueScore: Int,
    val falseScore: Int,
    val learnedScore: Int
)
