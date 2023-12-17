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
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.databinding.FragmentForgetPasswordBinding
import com.example.vocabularyapp.utils.AuthEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        viewModel.allEventsFlow.onEach { event ->
            when (event) {
                is AuthEvents.PasswordResetSent -> {
                    if (event.result) {
                        Toast.makeText(
                            requireContext(),
                            "Parola sıfırlama e-postası gönderildi",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Parola sıfırlama e-postası gönderilemedi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is AuthEvents.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Hata: ${event.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Log.e("ForgetPasswordFragment", "Beklenmeyen event: $event")
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleErrors() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

}