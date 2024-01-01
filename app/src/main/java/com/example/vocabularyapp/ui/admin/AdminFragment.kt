package com.example.vocabularyapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vocabularyapp.R
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.databinding.FragmentAdminBinding
import com.example.vocabularyapp.ui.home.HomeViewModel
import com.example.vocabularyapp.ui.savedwords.WordsAdapter
import com.example.vocabularyapp.utils.UiState

class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: AllWordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminBinding.inflate(inflater, container, false)

        setupWidgets()

        return binding.root
    }

    private fun setupWidgets() {
        binding.apply {
            fabAddWord.setOnClickListener {
                val action = AdminFragmentDirections.actionAdminFragmentToAddWordFragment()
                findNavController().navigate(action)
            }
            ivSignOut.setOnClickListener {
                findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
            }

            homeViewModel.getAllWords()
            homeViewModel.getAllWords.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        progressBar.visibility = View.GONE
                        initRecycler(it.data)
                    }
                    is UiState.Failure -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Gösterilecek kelime yok", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }


    private fun initRecycler(wordList: List<Word>) {
        adapter = AllWordsAdapter(wordList, AllWordsAdapter.OnClickListener {
            val bundle = Bundle().apply {
                putSerializable("word", it)
            }
            findNavController().navigate(
                R.id.action_adminFragment_to_wordDetailFragment,
                bundle
            )
        })
        binding.rvAdmin.adapter = adapter
    }


}