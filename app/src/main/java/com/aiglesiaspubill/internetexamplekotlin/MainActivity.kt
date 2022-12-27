package com.aiglesiaspubill.internetexamplekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.aiglesiaspubill.internetexamplekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.stateLiveData.observe(this) {
            when(it) {
                is MainActivityViewModel.MainActivityState.SuccessBootcampList -> {
                    binding.txView.text = it.toString()
                    binding.progressBar.visibility = View.INVISIBLE
                    viewModel.apiCallToken()
                }
                is MainActivityViewModel.MainActivityState.Error -> {
                    binding.txView.text = it.message
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is MainActivityViewModel.MainActivityState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                is MainActivityViewModel.MainActivityState.SuccessJwtTest -> {
                    binding.txView.text = it.token
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.getBootcamps()
    }

}