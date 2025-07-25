package ro.alingrosu.stockmanagement.presentation.ui.main.product.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentProductListBinding
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
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
    private val productAdapter = ProductAdapter(::onItemClicked)

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
        binding.rvProducts.adapter = productAdapter
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.apply {
                        rvProducts.isVisible = false
                        tvEmpty.isVisible = false
                        loading.isVisible = true
                    }
                }

                is UiState.Success -> {
                    binding.apply {
                        rvProducts.isVisible = uiState.data.isNotEmpty()
                        tvEmpty.isVisible = uiState.data.isEmpty()
                        loading.isVisible = uiState.data.isEmpty()
                    }
                    productAdapter.submitList(uiState.data)
                }

                is UiState.Error -> {
                    binding.apply {
                        rvProducts.isVisible = false
                        tvEmpty.isVisible = true
                        loading.isVisible = false
                    }
                    Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.loadData()
    }

    private fun onItemClicked(item: ProductUi) {
        findNavController().navigate(
            ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(item)
        )
    }
}