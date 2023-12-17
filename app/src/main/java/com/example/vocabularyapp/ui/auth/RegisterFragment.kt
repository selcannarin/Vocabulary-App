package com.example.vocabularyapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.databinding.FragmentRegisterBinding
import com.example.vocabularyapp.utils.AuthEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)
        (activity as MainActivity).setBottomNavVisibilityGone()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavVisibilityGone()

        setupListeners()
        handleEvents()
        handleErrors()

        return binding?.root
    }

    private fun setupListeners() {
        binding?.apply {
            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val surName = etSurname.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (name.isEmpty() || surName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Lütfen bütün alanları doldurun",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "Parolalar eşleşmiyor", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val fullName: String = "$name" + "  " + "$surName"

                viewModel.register(
                    fullName = fullName,
                    email = email,
                    password = password
                )
            }

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun handleEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allEventsFlow.collect { event ->
                when (event) {
                    is AuthEvents.UserRegistered -> {
                        if (event.user != null) {

                            Toast.makeText(
                                requireContext(),
                                "Kullanıcı başarıyla kaydedildi",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Kullanıcı kaydedilemedi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is AuthEvents.Error -> {
                        Log.e("RegisterFragment", "Hata: ${event.errorMessage}")
                        Toast.makeText(requireContext(), event.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun handleErrors() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}