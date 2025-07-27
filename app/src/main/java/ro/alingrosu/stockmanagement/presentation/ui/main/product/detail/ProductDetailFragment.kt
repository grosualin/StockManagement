package ro.alingrosu.stockmanagement.presentation.ui.main.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding4.widget.itemClickEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentProductDetailBinding
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class ProductDetailFragment : BaseFragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()

    private val viewModel: ProductDetailViewModel by viewModels {
        Factory {
            getAppComponent().productDetailViewModel
        }
    }
    private var selectedSupplier: SupplierUi? = null


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
        /**
         * If we receive ProductUi object through arguments means we are in edit mode
         * Otherwise, we consider to be in add mode
         */
        args.product?.let { product ->
            setEditMode(product)
        } ?: run {
            setAddMode()
        }
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { event ->
            event.contentIfNotHandled?.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.buttonSave.setLoading(true)
                    }

                    is UiState.Success -> {
                        binding.buttonSave.setLoading(false)
                        Toast.makeText(context, getString(R.string.product_save_success), Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }

                    is UiState.Error -> {
                        binding.buttonSave.setLoading(false)
                        Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setEditMode(product: ProductUi) {
        binding.apply {
            etName.setText(product.name)
            etDescription.setText(product.description)
            etPrice.setText(product.price.toString())
            etCategory.setText(product.category)
            etBarcode.setText(product.barcode)
            acSupplier.setText(product.supplier.name)
            acSupplier.isEnabled = false
            etCurrentStock.setText(product.currentStock.toString())
            etMinStock.setText(product.minStock.toString())
            buttonSave.setClickListener {
                viewModel.updateProduct(
                    product.copy(
                        name = etName.text.toString(),
                        description = etDescription.text.toString(),
                        price = etPrice.text.toString().toDouble(),
                        category = etCategory.text.toString(),
                        barcode = etBarcode.text.toString()
                    )
                )
            }
        }
    }

    private fun setAddMode() {
        viewModel.suppliers.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                it.map { it.name }
            )

            binding.acSupplier.setAdapter(adapter)
            binding.acSupplier.isEnabled = true
        }
        compositeDisposable.add(
            binding.acSupplier.itemClickEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->
                    selectedSupplier = viewModel.suppliers.value?.get(event.position)
                }, { error ->
                    error.printStackTrace()
                })
        )
        viewModel.fetchSuppliers()

        binding.etCurrentStock.isEnabled = true
        binding.etMinStock.isEnabled = true

        binding.buttonSave.setClickListener {
            viewModel.addProduct(
                ProductUi(
                    name = binding.etName.text.toString(),
                    description = binding.etDescription.text.toString(),
                    price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0,
                    category = binding.etCategory.text.toString(),
                    barcode = binding.etBarcode.text.toString(),
                    supplier = selectedSupplier ?: run {
                        Toast.makeText(context, getString(R.string.supplier_not_selected), Toast.LENGTH_LONG).show()
                        return@setClickListener
                    },
                    currentStock = binding.etCurrentStock.text.toString().toIntOrNull() ?: 0,
                    minStock = binding.etMinStock.text.toString().toIntOrNull() ?: 0
                )
            )
        }
    }
}