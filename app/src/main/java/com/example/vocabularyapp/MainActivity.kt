package com.example.vocabularyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.vocabularyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

 private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragmentContainerView)

        if (navController.currentDestination?.id == R.id.loginFragment) {
            binding.bottomNav.visibility = View.GONE
        } else {
            binding.bottomNav.visibility = View.VISIBLE

        }

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.savedWords -> {
                    navController.navigate(R.id.savedWordsFragment)
                    return@setOnItemSelectedListener true

                }

                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    return@setOnItemSelectedListener true

                }

                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    fun setBottomNavVisibilityGone() {
        binding.bottomNav.isVisible = false
    }


    fun setBottomNavVisibilityVisible() {
        binding.bottomNav.isVisible = true
    }
}