package ro.alingrosu.stockmanagement.presentation.ui.main.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentProductDetailBinding
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
        binding.apply {
            etName.setText(args.product.name)
            etDescription.setText(args.product.description)
            etPrice.setText(args.product.price.toString())
            etCategory.setText(args.product.category)
            etBarcode.setText(args.product.barcode)
            etSupplier.setText(args.product.supplier.name)
            etCurrentStock.setText(args.product.currentStock.toString())
            etMinStock.setText(args.product.minStock.toString())
            buttonSave.setOnClickListener {
                viewModel.saveProduct(
                    args.product.copy(
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

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.loading.isVisible = true
                    binding.buttonSave.isEnabled = false
                }

                is UiState.Success -> {
                    binding.loading.isVisible = false
                    binding.buttonSave.isEnabled = true
                    Toast.makeText(context, getString(R.string.product_save_success), Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }

                is UiState.Error -> {
                    Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}