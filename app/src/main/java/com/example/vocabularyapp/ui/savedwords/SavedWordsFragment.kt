package com.example.vocabularyapp.ui.savedwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.databinding.FragmentSavedWordsBinding

class SavedWordsFragment : Fragment() {

    private var _binding: FragmentSavedWordsBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedWordsBinding.inflate(inflater,container,false)
        (activity as MainActivity).setBottomNavVisibilityVisible()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}