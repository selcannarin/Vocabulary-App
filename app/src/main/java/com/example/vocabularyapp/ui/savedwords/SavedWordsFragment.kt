package com.example.vocabularyapp.ui.savedwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.databinding.FragmentSavedWordsBinding
import com.example.vocabularyapp.ui.home.HomeViewModel
import com.example.vocabularyapp.utils.UiState

class SavedWordsFragment : Fragment() {

    private var _binding: FragmentSavedWordsBinding? = null
    private val binding get() = _binding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: WordsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedWordsBinding.inflate(inflater,container,false)
        (activity as MainActivity).setBottomNavVisibilityVisible()

        binding?.rvSavedWords?.setHasFixedSize(true)
        binding?.rvSavedWords?.layoutManager = LinearLayoutManager(requireContext())
        getFavorites()
        setUpWidgets()
        return binding?.root
    }

    private fun setUpWidgets(){
        binding?.apply {
            btnLearned.setOnClickListener {
                getLearnedWords()
            }
            btnFavorites.setOnClickListener {
                getFavorites()
            }
        }
    }

    private fun initRecycler(wordList: List<Word>){
        adapter = WordsAdapter(wordList)
        binding?.rvSavedWords?.adapter = adapter
    }

    private fun getLearnedWords(){
        homeViewModel.getLearnedWords()
        homeViewModel.getLearnedWords.observe(viewLifecycleOwner){
            when(it)
            {
                is UiState.Loading-> {
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.rvSavedWords?.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding?.rvSavedWords?.visibility = View.VISIBLE
                    binding?.progressBar?.visibility = View.GONE
                    val words = it.data
                    initRecycler(words)
                }

                else -> {
                    binding?.rvSavedWords?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Öğrenilen kelimeniz bulunmuyor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getFavorites(){
        homeViewModel.getFavorites()
        homeViewModel.getFavorites.observe(viewLifecycleOwner){
            when(it)
            {
                is UiState.Loading-> {
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.rvSavedWords?.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.rvSavedWords?.visibility = View.VISIBLE
                    val words = it.data
                    initRecycler(words)
                }

                else -> {
                    binding?.rvSavedWords?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Favori kelimeniz bulunmuyor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}