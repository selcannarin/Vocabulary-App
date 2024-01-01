package com.example.vocabularyapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vocabularyapp.R
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.databinding.FragmentWordDetailBinding
import com.example.vocabularyapp.ui.home.HomeViewModel
import com.example.vocabularyapp.utils.UiState

class WordDetailFragment : Fragment() {

    private lateinit var binding: FragmentWordDetailBinding
    private val args: WordDetailFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordDetailBinding.inflate(inflater, container, false)

        val word = args.word
        initViews(word)
        setupWidgets()
        return binding.root
    }

    private fun initViews(word: Word) {
        with(binding) {
            tvTurkishWord.text = word.turkish
            tvEnglishWord.text = word.english
        }
    }

    private fun setupWidgets() {
        binding.apply {
            ivDeleteWord.setOnClickListener {
                deleteWord(args.word.id)
            }
            ivUpdateWord.setOnClickListener {
                tvTurkishWord.visibility = View.GONE
                tvEnglishWord.visibility = View.GONE
                etTurkishWord.visibility = View.VISIBLE
                etEnglishWord.visibility = View.VISIBLE
                btnSaveWord.visibility = View.VISIBLE

                etTurkishWord.setText(tvTurkishWord.text.toString())
                etEnglishWord.setText(tvEnglishWord.text.toString())

                btnSaveWord.setOnClickListener {
                    val turkish = etTurkishWord.text.toString()
                    val english = etEnglishWord.text.toString()
                    val word = Word(args.word.id, english, turkish)
                    updateWord(word)
                }
            }
        }
    }

    private fun updateWord(word: Word) {
        homeViewModel.updateWord(word)
        homeViewModel.updateWord.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Kelime güncellendi", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_wordDetailFragment_to_adminFragment)
                }

                is UiState.Failure -> {
                    Toast.makeText(requireContext(), "Kelime güncellenemedi", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun deleteWord(wordId: String) {
        homeViewModel.deleteWord(wordId)
        homeViewModel.deleteWord.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Kelime silindi", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_wordDetailFragment_to_adminFragment)
                }

                is UiState.Failure -> {
                    Toast.makeText(requireContext(), "Kelime silinemedi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}