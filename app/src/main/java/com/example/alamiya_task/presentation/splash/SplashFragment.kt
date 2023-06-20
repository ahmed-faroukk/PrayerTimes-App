package com.example.alamiya_task.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alamiya_task.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class splashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(3500)
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }
}