package ro.alingrosu.stockmanagement.presentation.ui.main.product.list

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentProductListBinding
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class ProductListFragment : BaseFragment(R.layout.fragment_product_list) {

    private lateinit var binding: FragmentProductListBinding

    private val viewModel: ProductListViewModel by viewModels {
        Factory {
            getAppComponent().productListViewModel
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentProductListBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
    }

    override fun listenFoUiState() {
    }
}