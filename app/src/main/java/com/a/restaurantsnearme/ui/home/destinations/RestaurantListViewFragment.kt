package com.a.restaurantsnearme.ui.home.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.a.restaurantsnearme.R
import com.a.restaurantsnearme.databinding.RestaurantListViewFragmentBinding
import com.a.restaurantsnearme.databinding.RestaurantListViewItemBinding
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.repository.RepositoryResult
import com.a.restaurantsnearme.ui.BaseFragment
import com.a.restaurantsnearme.ui.home.HomeViewModelFactory
import com.a.restaurantsnearme.ui.home.MapListData
import com.a.restaurantsnearme.utils.getColorFromAttr
import com.a.restaurantsnearme.utils.toPx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RestaurantListViewFragment : BaseFragment<RestaurantListViewFragmentBinding, RestaurantListViewModel>() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    private val adapter by lazy {
        RestaurantListAdapter { restaurantClick, restaurant ->
            when (restaurantClick) {
                RestaurantItemClick.Favorite -> viewModel.favoriteClicked(restaurant)
                RestaurantItemClick.Open -> viewModel.openDetailView(restaurant)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setFragmentResultListener(RestaurantMapViewFragment.RESULT_KEY) { _, bundle ->
            val mapListData = bundle.getSerializable(RestaurantMapViewFragment.MAP_LIST_ITEM_KEY) as MapListData
            viewModel.setMapListData(mapListData)
            binding?.searchView?.setQuery(mapListData.query, false)
        }

        with(RestaurantListViewFragmentBinding.inflate(layoutInflater)) {
            binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RestaurantListViewModel::class.java)

        binding?.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (searchView.hasFocus()) {
                        searchView.clearFocus()
                        true
                    } else {
                        false
                    }
                }
            })

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = true
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.onQueryTextChanged(newText)
                    return true
                }
            })

            openMapViewButton.setOnClickListener { viewModel.goToMapView() }

            searchView.isIconifiedByDefault = false
            searchView.setOnClickListener { searchView.isIconified = false }
        }

        viewModel.errorFetchingNearbySearchLiveData.observe(viewLifecycleOwner) {
            showErrorToast()
        }

        viewModel.placesLiveData.observe(viewLifecycleOwner) { repositoryResult ->
            when (repositoryResult) {
                is RepositoryResult.Error -> showErrorToast()
                is RepositoryResult.Success -> {
                    adapter.submitList(ArrayList(repositoryResult.data))
                }
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), getString(R.string.error_finding_places_message), Toast.LENGTH_LONG).show()
    }

    private class RestaurantListAdapter(val onClickListener: (restaurantItemClick: RestaurantItemClick, restaurant: Restaurant) -> Unit) :
        ListAdapter<Restaurant, RestaurantListViewHolder>(RESTAURANT_DIFF_UTILS) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            RestaurantListViewHolder(RestaurantListViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: RestaurantListViewHolder, position: Int) {
            holder.bind(getItem(position), onClickListener)
        }
    }

    private class RestaurantListViewHolder(val viewBinding: RestaurantListViewItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Restaurant, onClickListener: (restaurantItemClick: RestaurantItemClick, restaurant: Restaurant) -> Unit) {
            viewBinding.apply {
                val context = root.context
                root.setOnClickListener {
                    onClickListener(RestaurantItemClick.Open, item)
                }
                favoriteIconImageView.setOnClickListener {
                    onClickListener(RestaurantItemClick.Favorite, item)
                }
                val favoriteIconColor = if (item.isFavorite) {
                    context.getColorFromAttr(android.R.attr.colorControlActivated)
                } else {
                    context.getColorFromAttr(android.R.attr.colorControlHighlight)
                }
                favoriteIconImageView.setColorFilter(favoriteIconColor)
                nameTextView.text = item.name
                addressTextView.text = item.formattedAddress
                ratingTextView.text = item.rating.toString()
                priceLevelContainer.removeAllViews()
                for (i in 0 until item.priceLevel) {
                    ImageView(root.context).apply {
                        val layoutParams = LinearLayout.LayoutParams(20.toPx(), 20.toPx())
                        setLayoutParams(layoutParams)
                        setColorFilter(ContextCompat.getColor(context, R.color.quantum_lime900), android.graphics.PorterDuff.Mode.SRC_IN)
                        load(R.drawable.ic_dollar)
                        priceLevelContainer.addView(this)
                    }
                }
            }
        }
    }

    sealed class RestaurantItemClick {
        object Open : RestaurantItemClick()
        object Favorite : RestaurantItemClick()
    }

    companion object {
        private val RESTAURANT_DIFF_UTILS = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.placeId == newItem.placeId && oldItem.isFavorite == newItem.isFavorite
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.placeId == newItem.placeId && oldItem.isFavorite == newItem.isFavorite
            }
        }
    }
}