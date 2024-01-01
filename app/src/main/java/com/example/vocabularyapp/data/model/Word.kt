package com.example.vocabularyapp.data.model

import java.io.Serializable

data class Word(
    val id: String = "",
    val english: String = "",
    val turkish: String = ""
) : Serializable
