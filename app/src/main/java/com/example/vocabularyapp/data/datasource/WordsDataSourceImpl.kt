package com.example.vocabularyapp.data.datasource

import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WordsDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : WordsDataSource {

    override suspend fun getRandomWordFromFirestore(): UiState<Word?> {
        return try {
            val currentUser = firebaseAuth.currentUser ?: return UiState.Failure("User not found")

            val documents = firestore.collection("words").get().await()

            if (documents.isEmpty) {
                return UiState.Failure("No documents found")
            } else {
                val learnedWords = firestore.collection("users")
                    .document(currentUser.email ?: "")
                    .get()
                    .await()
                    .toObject(User::class.java)
                    ?.learned ?: listOf()

                val wordList = mutableListOf<Word>()

                // Öğrenilmiş kelimeleri kontrol et
                documents.forEach { document ->
                    val id = document.id
                    val turkish = document.getString("turkish") ?: ""
                    val english = document.getString("english") ?: ""

                    // Öğrenilmiş kelimeler listesinde mi kontrol et
                    if (!learnedWords.contains(id)) {
                        wordList.add(Word(id, english, turkish))
                    }
                }

                if (wordList.isEmpty()) {
                    return UiState.Failure("No new words to learn")
                } else {
                    val randomIndex = (0 until wordList.size).random()
                    UiState.Success(wordList[randomIndex])
                }
            }
        } catch (e: Exception) {
            UiState.Failure("Error: ${e.message}")
        }
    }


    override suspend fun getFavoriteWords(result: (UiState<List<Word>>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            result(UiState.Failure("User not found"))
            return
        }

        try {
            val userEmail = currentUser.email

            val userDocRef = userEmail?.let { firestore.collection("users").document(it) }
            val userDocSnapshot = userDocRef?.get()?.await()

            if (userDocSnapshot != null && userDocSnapshot.exists()) {
                val favorites = userDocSnapshot.data?.get("favorites") as List<*>

                val wordList = mutableListOf<Word>()
                for (wordId in favorites) {
                    val wordDocRef = firestore.collection("words").document(wordId.toString())
                    val wordDocSnapshot = wordDocRef.get().await()

                    if (wordDocSnapshot.exists()) {
                        val word = wordDocSnapshot.toObject(Word::class.java)
                        word?.let { wordList.add(it) }
                    }
                }

                result(UiState.Success(wordList))
            } else {
                result(UiState.Failure("User data not found"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Failed to get favorite words: ${e.message}"))
        }
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
                    result(UiState.Success(false))
                }
            }.addOnFailureListener { exception ->
                result(UiState.Failure(exception.message ?: "Bir hata oluştu"))
            }
        } catch (e: Exception) {
            result(UiState.Failure(e.message ?: "Bir hata oluştu"))
        }
    }

    override suspend fun isFavorite(wordId: String, result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("User not found"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)

        try {
            val userDoc = userDocRef.get().await()
            val favorites = userDoc.get("favorites") as? List<*>

            val isFavorite = favorites?.contains(wordId) ?: false
            result(UiState.Success(isFavorite))
        } catch (e: Exception) {
            result(UiState.Failure("Favori durumunu kontrol ederken hata oldu: ${e.message}"))
        }
    }


    override suspend fun addTrueScore(result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)
        try {
            userDocRef.update("trueScore", FieldValue.increment(1)).addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener {
                result(UiState.Failure("trueScore güncellenirken bir hata oluştu: ${it.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Beklenmeyen bir hata oluştu: ${e.message}"))
        }
    }

    override suspend fun addFalseScore(result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)
        try {
            userDocRef.update("falseScore", FieldValue.increment(1)).addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener {
                result(UiState.Failure("falseScore güncellenirken bir hata oluştu: ${it.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Beklenmeyen bir hata oluştu: ${e.message}"))
        }
    }

    override suspend fun addLearnedScore(result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)
        try {
            userDocRef.update("learnedScore", FieldValue.increment(1)).addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener {
                result(UiState.Failure("learnedScore güncellenirken bir hata oluştu: ${it.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Beklenmeyen bir hata oluştu: ${e.message}"))
        }
    }

    override suspend fun getLearnedWords(result: (UiState<List<Word>>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            result(UiState.Failure("User not found"))
            return
        }

        try {
            val userEmail = currentUser.email


            val userDocRef = userEmail?.let { firestore.collection("users").document(it) }
            val userDocSnapshot = userDocRef?.get()?.await()

            if (userDocSnapshot != null && userDocSnapshot.exists()) {
                val learned = userDocSnapshot.data?.get("learned") as List<*>

                val wordList = mutableListOf<Word>()
                for (wordId in learned) {
                    val wordDocRef = firestore.collection("words").document(wordId.toString())
                    val wordDocSnapshot = wordDocRef.get().await()

                    if (wordDocSnapshot.exists()) {
                        val word = wordDocSnapshot.toObject(Word::class.java)
                        word?.let { wordList.add(it) }
                    }
                }

                result(UiState.Success(wordList))
            } else {
                result(UiState.Failure("User data not found"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Failed to get favorite words: ${e.message}"))
        }
    }

    override suspend fun addToLearnedWords(wordId: String, result: (UiState<Boolean>) -> Unit) {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (currentUserEmail == null) {
            result(UiState.Failure("Kullanıcı bulunamadı"))
            return
        }

        val userDocRef = firestore.collection("users").document(currentUserEmail)

        try {
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                val currentData = documentSnapshot?.data
                val currentLearned = currentData?.get("learned") as? ArrayList<String>
                    ?: arrayListOf()

                if (!currentLearned.contains(wordId)) {
                    currentLearned.add(wordId)

                    val updateData = hashMapOf(
                        "learned" to currentLearned
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

    override suspend fun getAllWords(result: (UiState<List<Word>>) -> Unit) {
        try {
            val documents = firestore.collection("words").get().await()

            if (documents.isEmpty) {
                result(UiState.Failure("No documents found"))
            } else {
                val wordList = mutableListOf<Word>()

                documents.forEach { document ->
                    val id = document.id
                    val turkish = document.getString("turkish") ?: ""
                    val english = document.getString("english") ?: ""

                    wordList.add(Word(id, english, turkish))
                }

                result(UiState.Success(wordList))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Error: ${e.message}"))
        }
    }

    override suspend fun addWord(word: Word, result: (UiState<Boolean>) -> Unit) {
        try {
            val wordMap = hashMapOf(
                "english" to word.english,
                "turkish" to word.turkish
            )

            firestore.collection("words").add(wordMap).addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener {
                result(UiState.Failure("Kelime eklenirken bir hata oluştu: ${it.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Beklenmeyen bir hata oluştu: ${e.message}"))
        }
    }

    override suspend fun deleteWord(wordId: String, result: (UiState<Boolean>) -> Unit) {
        try {
            firestore.collection("words").document(wordId).delete().addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener {
                result(UiState.Failure("Kelime silinirken bir hata oluştu: ${it.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Beklenmeyen bir hata oluştu: ${e.message}"))
        }
    }

    override suspend fun updateWord(word: Word, result: (UiState<Boolean>) -> Unit) {
        try {
            val wordMap = hashMapOf(
                "english" to word.english,
                "turkish" to word.turkish
            )

            firestore.collection("words").document(word.id).update(wordMap as Map<String, Any>).addOnSuccessListener {
                result(UiState.Success(true))
            }.addOnFailureListener {
                result(UiState.Failure("Kelime güncellenirken bir hata oluştu: ${it.message}"))
            }
        } catch (e: Exception) {
            result(UiState.Failure("Beklenmeyen bir hata oluştu: ${e.message}"))
        }
    }

}