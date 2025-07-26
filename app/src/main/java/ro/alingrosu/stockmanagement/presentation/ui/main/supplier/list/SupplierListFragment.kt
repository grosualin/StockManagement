package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentSupplierListBinding
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent
import java.util.concurrent.TimeUnit

class SupplierListFragment : BaseFragment(R.layout.fragment_supplier_list) {

    private lateinit var binding: FragmentSupplierListBinding

    private val viewModel: SupplierListViewModel by viewModels {
        Factory {
            getAppComponent().supplierListViewModel
        }
    }
    private val supplierAdapter = SupplierAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentSupplierListBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
        binding.rvSuppliers.adapter = supplierAdapter
        compositeDisposable.add(
            binding.fabAddSupplier.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    findNavController().navigate(
                        SupplierListFragmentDirections.actionSupplierListFragmentToSupplierDetailFragment(null)
                    )
                }, {
                    it.printStackTrace()
                })
        )
        compositeDisposable.add(
            binding.svSuppliers.queryTextChanges()
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.toString() }
                .subscribe({
                    viewModel.fetchSuppliers(it)
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
                        rvSuppliers.isVisible = false
                        tvEmpty.isVisible = false
                        loading.isVisible = true
                    }
                }

                is UiState.Success -> {
                    binding.apply {
                        rvSuppliers.isVisible = uiState.data.isNotEmpty()
                        tvEmpty.isVisible = uiState.data.isEmpty()
                        loading.isVisible = false
                    }
                    supplierAdapter.submitList(uiState.data)
                }

                is UiState.Error -> {
                    binding.apply {
                        rvSuppliers.isVisible = false
                        tvEmpty.isVisible = true
                        loading.isVisible = false
                    }
                    Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.fetchSuppliers("")
    }

    private fun onItemClicked(item: SupplierUi) {
        findNavController().navigate(
            SupplierListFragmentDirections.actionSupplierListFragmentToSupplierDetailFragment(item)
        )
    }
}