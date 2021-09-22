package com.a.restaurantsnearme.ui.home.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.a.restaurantsnearme.databinding.CheckForLocationPermissionsFragmentBinding
import com.a.restaurantsnearme.ui.BaseFragment
import com.a.restaurantsnearme.ui.home.HomeViewModelFactory
import com.a.restaurantsnearme.utils.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckForLocationPermissionsFragment : BaseFragment<CheckForLocationPermissionsFragmentBinding, CheckForLocationPermissionsViewModel>() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    @Inject
    lateinit var permissionHelper: PermissionHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        with(CheckForLocationPermissionsFragmentBinding.inflate(layoutInflater)) {
            binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CheckForLocationPermissionsViewModel::class.java)
        if (permissionHelper.hasLocationPermissions()) {
            viewModel.userHasLocationPermissions()
        } else {
            viewModel.userDoesNotHaveLocationPermissions()
        }
    }
}