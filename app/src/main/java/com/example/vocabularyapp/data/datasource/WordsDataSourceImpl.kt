package com.example.vocabularyapp.data.datasource

import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WordsDataSourceImpl@Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : WordsDataSource {

    override suspend fun getRandomWordFromFirestore(): UiState<Word?> {
        return try {
            val documents = firestore.collection("words").get().await()

            if (documents.isEmpty) {
                UiState.Failure("No documents found")
            } else {
                val totalDocuments = documents.size()
                val randomIndex = (0 until totalDocuments).random()
                val randomDocument = documents.documents[randomIndex]

                val id = randomDocument.id
                val turkish = randomDocument.getString("turkish") ?: ""
                val english = randomDocument.getString("english") ?: ""

                UiState.Success(Word(id, english, turkish))
            }
        } catch (e: Exception) {
            UiState.Failure("Error: ${e.message}")
        }
    }



    override suspend fun getFavoriteWords(): List<Word> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)

        try {
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                val currentData = documentSnapshot?.data
                val currentFavorites = currentData?.get("favorites") as? ArrayList<String>
                    ?: arrayListOf()

                if (!currentFavorites.contains(wordId)) {
                    currentFavorites.add(wordId)

                    val updateData = hashMapOf(
                        "favorites" to currentFavorites
                    )

                    userDocRef.update(updateData as Map<String, Any>).addOnSuccessListener {
                        result(UiState.Success(true))
                    }.addOnFailureListener { exception ->
                        result(UiState.Failure(exception.message ?: "Bir hata oluştu"))
                    }
                } else {
                    result(UiState.Success(false))
                }
            }.addOnFailureListener { exception ->
                result(UiState.Failure(exception.message ?: "Bir hata oluştu"))
            }
        } catch (e: Exception) {
            result(UiState.Failure(e.message ?: "Bir hata oluştu"))
        }
    }

    override suspend fun removeFromFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)

        try {
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                val currentData = documentSnapshot?.data
                val currentFavorites = currentData?.get("favorites") as? ArrayList<String>
                    ?: arrayListOf()

                if (currentFavorites.contains(wordId)) {
                    currentFavorites.remove(wordId)

                    val updateData = hashMapOf(
                        "favorites" to currentFavorites
                    )

                    userDocRef.update(updateData as Map<String, Any>).addOnSuccessListener {
                        result(UiState.Success(true))
                    }.addOnFailureListener { exception ->
                        result(UiState.Failure(exception.message ?: "Bir hata oluştu"))
                    }
                } else {
                    result(UiState.Success(false)) // Kelime zaten favorilerde yok
                }
            }.addOnFailureListener { exception ->
                result(UiState.Failure(exception.message ?: "Bir hata oluştu"))
            }
        } catch (e: Exception) {
            result(UiState.Failure(e.message ?: "Bir hata oluştu"))
        }
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