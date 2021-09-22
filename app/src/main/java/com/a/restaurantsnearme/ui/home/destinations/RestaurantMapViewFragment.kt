package com.a.restaurantsnearme.ui.home.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.a.restaurantsnearme.R
import com.a.restaurantsnearme.databinding.RestaurantMapViewFragmentBinding
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.repository.RepositoryResult
import com.a.restaurantsnearme.ui.BaseFragment
import com.a.restaurantsnearme.ui.home.HomeViewModelFactory
import com.a.restaurantsnearme.ui.home.MapListData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RestaurantMapViewFragment : BaseFragment<RestaurantMapViewFragmentBinding, RestaurantMapViewModel>() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    private val args by navArgs<RestaurantMapViewFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        with(RestaurantMapViewFragmentBinding.inflate(layoutInflater)) {
            binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RestaurantMapViewModel::class.java)
        viewModel.setMapListData(args.mapListData)

        binding?.apply {
            mapView.onCreate(savedInstanceState)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = true
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.onQueryTextChanged(newText)
                    return true
                }
            })

            searchView.setQuery(args.mapListData.query, false)
            searchView.isIconifiedByDefault = false
            searchView.setOnClickListener { searchView.isIconified = false }

            openListViewButton.setOnClickListener { viewModel.goToListView() }

            lifecycleScope.launchWhenCreated {
                val googleMap = mapView.awaitMap().apply {
                    isTrafficEnabled = false
                }
                viewModel.onMapInitialized()

                viewModel.placesLiveData.observe(viewLifecycleOwner) { repositoryResult ->
                    when (repositoryResult) {
                        is RepositoryResult.Error -> showErrorToast()
                        is RepositoryResult.Success -> {
                            addMarketsToMap(googleMap, repositoryResult.data)
                            Bundle().apply {
                                putSerializable(MAP_LIST_ITEM_KEY, MapListData(searchView.query.toString(), repositoryResult.data))
                                setFragmentResult(RESULT_KEY, this)
                            }
                        }
                    }
                }
            }

            viewModel.errorFetchingNearbySearchLiveData.observe(viewLifecycleOwner) {
                showErrorToast()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView?.onDestroy()
    }

    private fun addMarketsToMap(googleMap: GoogleMap, data: List<Restaurant>) {
        googleMap.apply {
            clear()
            data.forEach { restaurant ->
                val markerOptions = MarkerOptions()
                val latLng = LatLng(restaurant.geometry.location.lat, restaurant.geometry.location.lng)
                moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 14f, 0f, 0f)))
                markerOptions.position(latLng)
                markerOptions.title(restaurant.name)
                animateCamera(CameraUpdateFactory.newLatLng(latLng))
                addMarker(markerOptions)
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), getString(R.string.error_finding_places_message), Toast.LENGTH_LONG).show()
    }

    companion object {
        const val RESULT_KEY = "RestaurantMapViewFragment.Result"
        const val MAP_LIST_ITEM_KEY = "RestaurantMapViewFragment.Result.MapListData"
    }
}