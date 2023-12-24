package com.example.vocabularyapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.databinding.FragmentProfileBinding
import com.example.vocabularyapp.ui.auth.AuthViewModel
import com.example.vocabularyapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private val authViewModel: AuthViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavVisibilityVisible()

        setupListeners()
        setupObservers()

        return binding?.root
    }

    private fun setupListeners() {
        binding?.apply {
            btnSignOut.setOnClickListener {
                authViewModel.signOut()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            ivEdit.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
        }
    }

    private fun setupObservers() {
        binding?.apply {
            authViewModel.getUser()
            authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                tvEmail.text = user?.email
                profileViewModel.getUserData(user?.email.toString())
                profileViewModel.getUserData.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Success -> {
                            tvFullName.setText(it.data.fullName)
                            tvTrueScore.text = it.data.trueScore.toString()
                            tvFalseScore.text = it.data.falseScore.toString()
                            tvLearnedScore.text = it.data.learnedScore.toString()
                        }
                        else -> {}
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}