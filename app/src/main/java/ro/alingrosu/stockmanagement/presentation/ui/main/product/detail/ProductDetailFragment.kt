package ro.alingrosu.stockmanagement.presentation.ui.main.product.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentDashboardBinding
import ro.alingrosu.stockmanagement.databinding.FragmentProductDetailBinding
import ro.alingrosu.stockmanagement.presentation.ui.main.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.ui.main.dashboard.DashboardViewModel
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class ProductDetailFragment : BaseFragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding

    private val viewModel: ProductDetailViewModel by viewModels {
        Factory {
            getAppComponent().productDetailViewModel
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentProductDetailBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
    }

    override fun listenFoUiState() {
    }
}