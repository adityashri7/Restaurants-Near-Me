package com.a.restaurantsnearme.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, V : BaseViewModel> : Fragment() {
    protected var binding: B? = null
    protected lateinit var viewModel: V

    override fun onStart() {
        super.onStart()
        viewModel.getNavigationEvent().observe(viewLifecycleOwner) { navDirections ->
            findNavController().navigate(navDirections)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}