package com.example.vocabularyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.databinding.FragmentHomeBinding
import com.example.vocabularyapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by activityViewModels()

    var tvTurkishTextView: TextView? = null
    var tvEnglishTextView: TextView? = null

    var currentWord: Word? = null

    private var isFavorite = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tvTurkish = binding.root.findViewById<TextView>(R.id.tvBackTurkish)
        var tvEnglish = binding.root.findViewById<TextView>(R.id.tvFrontEnglish)

        tvTurkishTextView = tvTurkish
        tvEnglishTextView = tvEnglish
        setFlipCard()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        (requireActivity() as MainActivity).setBottomNavVisibilityVisible()

        setUpWidgets()

        return binding.root
    }

    private fun setFlipCard() {
        binding.flashCard.setOnClickListener {
            binding.flashCard.flipTheView()
        }
        viewModel.getRandomeWordFromFirestore()
        viewModel.randomWord.observe(viewLifecycleOwner) {

            currentWord = it
            tvTurkishTextView?.text = it?.turkish
            tvEnglishTextView?.text = it?.english

            viewModel.isFavorite(it?.id!!)
            viewModel.isFavorite.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        if (it.data) {
                            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_yes)
                            isFavorite = true
                        } else {
                            binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                            isFavorite = false
                        }
                    }

                    is UiState.Failure -> {
                        Toast.makeText(
                            requireContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT
                        ).show()

                    }
                }

            }

        }
    }

    private fun setUpWidgets() {
        binding?.apply {

            ivFavorite.setOnClickListener {
                favoriteControl()
            }
            tvNext.setOnClickListener {
                nextWord()
            }
            btnTrue.setOnClickListener {
                trueControl()
            }
            btnFalse.setOnClickListener {
                falseControl()
            }
            btnLearned.setOnClickListener {
                addToLearnedWords()
            }
        }
    }

    private fun nextWord() {
        viewModel.getRandomeWordFromFirestore()
        viewModel.randomWord.observe(viewLifecycleOwner) {
            currentWord = it
            tvTurkishTextView?.text = it?.turkish
            tvEnglishTextView?.text = it?.english
        }
    }

    private fun trueControl() {
        viewModel.addTrueScore()
        viewModel.addTrueScore.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    Toast.makeText(
                        requireContext(), "Doğru olarak işaretlendi.", Toast.LENGTH_SHORT
                    ).show()
                    nextWord()
                }

                is UiState.Failure -> {
                    Toast.makeText(
                        requireContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun falseControl() {
        viewModel.addFalseScore()
        viewModel.addFalseScore.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    Toast.makeText(
                        requireContext(), "Yanlış olarak işaretlendi.", Toast.LENGTH_SHORT
                    ).show()
                    nextWord()
                }

                is UiState.Failure -> {
                    Toast.makeText(
                        requireContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun favoriteControl() {
        binding?.apply {

            if (!isFavorite) {
                viewModel.addToFavorite(currentWord!!.id)
                viewModel.addToFavorite.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Loading -> {

                        }

                        is UiState.Success -> {
                            if (it.data) {
                                ivFavorite.setImageResource(R.drawable.ic_favorite_yes)
                                Toast.makeText(
                                    requireContext(),
                                    "Kelime favorilere eklendi.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isFavorite = true
                            }
                        }

                        is UiState.Failure -> {
                            Toast.makeText(
                                requireContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            } else {
                viewModel.removeFromFavorite(currentWord!!.id)
                viewModel.removeFromFavorite.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Loading -> {

                        }

                        is UiState.Success -> {
                            if (it.data) {
                                ivFavorite.setImageResource(R.drawable.ic_favorite)
                                Toast.makeText(
                                    requireContext(),
                                    "Kelime favorilerden çıkarıldı.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isFavorite = false
                            }
                        }

                        is UiState.Failure -> {
                            Toast.makeText(
                                requireContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }


        }

    }

    private fun addToLearnedWords() {
        viewModel.addLearnedWord(currentWord!!.id)
        viewModel.addLearnedWord.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    Toast.makeText(
                        requireContext(), "Öğrenilen kelimelere eklendi.", Toast.LENGTH_SHORT
                    ).show()
                    viewModel.addLearnedScore()
                    nextWord()
                }

                is UiState.Failure -> {
                    Toast.makeText(
                        requireContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }



    //Sözcük listesini db ye kaydetme işlemi 1 kez yapıldı


    /* suspend fun saveWordsToFirestoreFromAssets(context: Context, fileName: String) {
         val jsonString = loadJSONFromAssets(context, fileName)
         jsonString?.let {
             val jsonArray = JSONArray(it)

             for (i in 0 until jsonArray.length()) {
                 val wordObject = jsonArray.getJSONObject(i)
                 val english = wordObject.getString("english")
                 val turkish = wordObject.getString("turkish")

                 val wordData = hashMapOf(
                     "english" to english,
                     "turkish" to turkish
                 )

                 FirebaseFirestore.getInstance().collection("words").add(wordData).await()
             }
         }
     }*/

    /*private fun loadJSONFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }*/

}