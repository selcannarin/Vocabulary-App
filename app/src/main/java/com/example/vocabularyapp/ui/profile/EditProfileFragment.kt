package com.example.vocabularyapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.data.model.User
import com.example.vocabularyapp.databinding.FragmentEditProfileBinding
import com.example.vocabularyapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var updatedData: User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater)

        (activity as MainActivity).setBottomNavVisibilityVisible()

        setUpListeners()

        return binding.root
    }

    private fun setUpListeners() {
        binding.apply {

            profileViewModel.getUserData.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Success -> {

                        updatedData = it.data
                    }

                    else -> {}
                }
            }

            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val surname = etSurname.text.toString()

                if (name.isEmpty()) {
                    etName.error = "Name cannot be empty"
                    return@setOnClickListener
                }

                if (surname.isEmpty()) {
                    etSurname.error = "Surname cannot be empty"
                    return@setOnClickListener
                }

                val fullName = "$name $surname"

                updatedData = updatedData?.copy(fullName = fullName)

                updatedData?.let { data ->
                    profileViewModel.editUserData(data)
                    profileViewModel.editUserData.observe(viewLifecycleOwner) {
                        when (it) {
                            is UiState.Success -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Kullanıcı bilgileri güncellendi.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                            }

                            else -> {}
                        }
                    }
                }
            }
        }


    }


}
