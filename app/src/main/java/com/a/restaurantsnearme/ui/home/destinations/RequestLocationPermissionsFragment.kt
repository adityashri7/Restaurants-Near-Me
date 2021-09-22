package com.a.restaurantsnearme.ui.home.destinations

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.a.restaurantsnearme.R
import com.a.restaurantsnearme.databinding.RequestLocationPermissionsFragmentBinding
import com.a.restaurantsnearme.ui.BaseFragment
import com.a.restaurantsnearme.ui.home.HomeViewModelFactory
import com.a.restaurantsnearme.utils.PermissionHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RequestLocationPermissionsFragment : BaseFragment<RequestLocationPermissionsFragmentBinding, RequestLocationPermissionsViewModel>() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    @Inject
    lateinit var permissionHelper: PermissionHelper

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.userHasLocationAccess()
        } else {
            showSettingsPrompt()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        with(RequestLocationPermissionsFragmentBinding.inflate(layoutInflater)) {
            binding = this
            return root
        }
    }

    private fun showSettingsPrompt() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.locationPermissionsDeniedMessage)
            .setTitle(R.string.locationPermissionsDeniedTitle)
            .setPositiveButton(R.string.locationPermissionsDeniedSettingsButton) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RequestLocationPermissionsViewModel::class.java)
        if (permissionHelper.hasLocationPermissions()) {
            viewModel.userHasLocationAccess()
        }

        binding?.apply {
            accessLocationButton.setOnClickListener {
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}