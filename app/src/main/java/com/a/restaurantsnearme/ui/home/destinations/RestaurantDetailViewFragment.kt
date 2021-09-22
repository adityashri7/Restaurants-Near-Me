package com.a.restaurantsnearme.ui.home.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.a.restaurantsnearme.R
import com.a.restaurantsnearme.databinding.RestaurantDetailFragmentBinding
import com.a.restaurantsnearme.ui.BaseFragment
import com.a.restaurantsnearme.ui.home.HomeViewModelFactory
import com.a.restaurantsnearme.utils.toPx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantDetailViewFragment : BaseFragment<RestaurantDetailFragmentBinding, RestaurantDetailViewModel>() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    private val args by navArgs<RestaurantDetailViewFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        with(RestaurantDetailFragmentBinding.inflate(layoutInflater)) {
            binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel::class.java)
        val restaurant = args.restaurant
        binding?.apply {
            toolbar.title = getString(R.string.restaurantDetailTitle)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            nameTextView.text = restaurant.name
            addressTextView.text = restaurant.formattedAddress
            ratingTextView.text = restaurant.rating.toString()
            ratingTextView.contentDescription = getString(R.string.ratingContentDescription, restaurant.rating.toString())
            ratingIconImageView.contentDescription = getString(R.string.ratingContentDescription, restaurant.rating.toString())
            priceLevelContainer.contentDescription = getString(R.string.priceContentDescription, restaurant.priceLevel.toString())
            priceLevelContainer.removeAllViews()
            for (i in 0 until restaurant.priceLevel) {
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