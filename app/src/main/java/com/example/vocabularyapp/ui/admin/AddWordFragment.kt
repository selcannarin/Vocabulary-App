package com.example.vocabularyapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.databinding.FragmentAddWordBinding
import com.example.vocabularyapp.ui.home.HomeViewModel
import com.example.vocabularyapp.utils.UiState

class AddWordFragment : Fragment() {

    private lateinit var binding: FragmentAddWordBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddWordBinding.inflate(inflater, container, false)

        setupWidgets()

        return binding.root
    }

    private fun setupWidgets() {
        binding.apply {
            btnSave.setOnClickListener {

                val turkish = etTurkish.text.toString()
                val english = etEnglish.text.toString()

                if (turkish.isEmpty()) {
                    Toast.makeText(requireContext(), "Türkçe kelimeyi giriniz", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (english.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "İngilizce kelimeyi giriniz",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                addWord(Word("", english, turkish))
            }
        }
    }

    private fun addWord(word: Word) {
        homeViewModel.addWord(word)
        homeViewModel.addWord.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Kelime eklendi", Toast.LENGTH_SHORT).show()
                    binding.etTurkish.setText("")
                    binding.etEnglish.setText("")
                }

                is UiState.Failure -> {
                    Toast.makeText(requireContext(), "Kelime eklenemedi", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
}