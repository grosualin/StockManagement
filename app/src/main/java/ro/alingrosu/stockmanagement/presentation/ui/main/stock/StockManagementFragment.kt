package ro.alingrosu.stockmanagement.presentation.ui.main.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import com.jakewharton.rxbinding4.widget.itemClickEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentStockManagementBinding
import ro.alingrosu.stockmanagement.domain.model.TransactionType
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.model.TransactionUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent
import java.util.Date

class StockManagementFragment : BaseFragment(R.layout.fragment_stock_management) {

    private lateinit var binding: FragmentStockManagementBinding

    private val viewModel: StockManagementViewModel by viewModels {
        Factory {
            getAppComponent().stockManagementViewModel
        }
    }
    private var selectedProduct: ProductUi? = null
    private var selectedType: TransactionType? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentStockManagementBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
        viewModel.products.observe(viewLifecycleOwner) {
            val adapterProducts = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                it.map { it.name }
            )
            binding.acProduct.setAdapter(adapterProducts)
        }
        compositeDisposable.add(
            binding.acProduct.itemClickEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->
                    selectedProduct = viewModel.products.value?.get(event.position)
                }, { error ->
                    error.printStackTrace()
                })
        )

        val adapterOperations = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            TransactionType.entries.map { it.name.uppercase() }
        )
        binding.acOperation.setAdapter(adapterOperations)
        compositeDisposable.add(
            binding.acOperation.itemClickEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->
                    selectedType = TransactionType.entries[event.position]
                }, { error ->
                    error.printStackTrace()
                })
        )

        viewModel.fetchProducts()

        binding.buttonSave.setClickListener {
            viewModel.addStockTransaction(
                TransactionUi(
                    date = Date().time,
                    type = selectedType ?: run {
                        Toast.makeText(context, getString(R.string.type_not_selected), Toast.LENGTH_LONG).show()
                        return@setClickListener
                    },
                    product = selectedProduct ?: run {
                        Toast.makeText(context, getString(R.string.product_not_selected), Toast.LENGTH_LONG).show()
                        return@setClickListener
                    },
                    quantity = binding.etQuantity.text.toString().toIntOrNull() ?: run {
                        Toast.makeText(context, getString(R.string.qty_not_selected), Toast.LENGTH_LONG).show()
                        return@setClickListener
                    },
                    notes = binding.etNotes.text.toString()
                )
            )
        }
    }

    override fun listenFoUiState() {
        viewModel.uiState.distinctUntilChanged().observe(viewLifecycleOwner) { event ->
            event.contentIfNotHandled?.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.buttonSave.setLoading(true)
                    }

                    is UiState.Success -> {
                        binding.buttonSave.setLoading(false)
                        binding.acProduct.text.clear()
                        binding.acOperation.text.clear()
                        binding.etQuantity.text?.clear()
                        binding.etNotes.text?.clear()
                        selectedType = null
                        selectedProduct = null
                        Toast.makeText(context, getString(R.string.transaction_save_success), Toast.LENGTH_LONG).show()
                    }

                    is UiState.Error -> {
                        binding.buttonSave.setLoading(false)
                        Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}