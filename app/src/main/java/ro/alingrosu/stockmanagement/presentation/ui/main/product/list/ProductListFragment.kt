package ro.alingrosu.stockmanagement.presentation.ui.main.product.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentProductListBinding
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent
import java.util.concurrent.TimeUnit

class ProductListFragment : BaseFragment(R.layout.fragment_product_list) {

    private lateinit var binding: FragmentProductListBinding
    private val compositeDisposable = CompositeDisposable()

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

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun initView() {
        binding.rvProducts.adapter = productAdapter

        compositeDisposable.add(
            Observable.create<String> { emitter ->
                binding.svProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = false
                    override fun onQueryTextChange(newText: String?): Boolean {
                        emitter.onNext(newText.orEmpty())
                        return true
                    }
                })
            }.debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.fetchProducts(it)
                }, {
                    it.printStackTrace()
                })
        )
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
                        loading.isVisible = false
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
        viewModel.fetchProducts("")
    }

    private fun onItemClicked(item: ProductUi) {
        findNavController().navigate(
            ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(item)
        )
    }
}