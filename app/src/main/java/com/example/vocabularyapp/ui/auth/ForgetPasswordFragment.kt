package com.example.vocabularyapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.databinding.FragmentForgetPasswordBinding
import com.example.vocabularyapp.utils.AuthEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgetPasswordBinding

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgetPasswordBinding.bind(view)
        (activity as MainActivity).setBottomNavVisibilityGone()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavVisibilityGone()

        setupListeners()
        handleEvents()
        handleErrors()

        return binding.root
    }

    private fun setupListeners() {
        binding?.apply {
            btnSendResetMail.setOnClickListener {
                val email = etEmail.text.toString()
                if (email.isEmpty()) {
                    etEmail.error = "Email boş olmamalıdır."
                    return@setOnClickListener
                }

                viewModel.sendPasswordReset(email)

            }
        }
    }

    private fun handleEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allEventsFlow.collect { event ->
                when (event) {
                    is AuthEvents.PasswordResetSent -> {
                        if (event.result) {
                            Toast.makeText(
                                requireContext(),
                                "Şifre sıfırlama e-postası gönderildi",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Şifre sıfırlama e-postası gönderilemedi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is AuthEvents.Error -> {
                        Log.e("ForgetPasswordFragment", "Hata: ${event.errorMessage}")
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

}