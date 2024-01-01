package com.example.vocabularyapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vocabularyapp.MainActivity
import com.example.vocabularyapp.R
import com.example.vocabularyapp.databinding.FragmentLoginBinding
import com.example.vocabularyapp.utils.AuthEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        onBackPressed()
        setupListeners()
        handleEvents()
        handleErrors()

        return binding?.root
    }

    private fun setupListeners() {
        binding?.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isEmpty()) {
                    etEmail.error = "Email boş olmamalıdır"
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    etPassword.error = "Parola boş olmamalıdır"
                    return@setOnClickListener
                }

                if(email == "admin" && password == "123456"){
                    findNavController().navigate(R.id.action_loginFragment_to_adminFragment)
                }
                else{
                    viewModel.signInWithEmailPassword(email, password)
                }



            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgetPassword.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            }
        }
    }

    private fun handleEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allEventsFlow.collect { event ->
                when (event) {
                    is AuthEvents.UserSignedIn -> {
                        Toast.makeText(requireContext(), "Giriş başarılı!", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                    is AuthEvents.Error -> {
                        Log.e("LoginFragment", "Hata: ${event.errorMessage}")
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomNavVisibilityGone()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

}