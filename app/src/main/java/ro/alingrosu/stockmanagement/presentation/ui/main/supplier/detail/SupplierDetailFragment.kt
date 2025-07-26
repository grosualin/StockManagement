package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentSupplierDetailBinding
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class SupplierDetailFragment : BaseFragment(R.layout.fragment_supplier_detail) {

    private lateinit var binding: FragmentSupplierDetailBinding
    private val args: SupplierDetailFragmentArgs by navArgs()

    private val viewModel: SupplierDetailViewModel by viewModels {
        Factory {
            getAppComponent().supplierDetailViewModel
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentSupplierDetailBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
        /**
         * If we receive ProductUi object through arguments means we are in edit mode
         * Otherwise, we consider to be in add mode
         */
        args.supplier?.let { product ->
            setEditMode(product)
        } ?: run {
            setAddMode()
        }
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
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

    private fun setEditMode(supplier: SupplierUi) {
        binding.apply {
            etName.setText(supplier.name)
            etContactPerson.setText(supplier.contactPerson)
            etPhone.setText(supplier.phone)
            etEmail.setText(supplier.email)
            etAddress.setText(supplier.address)
            buttonSave.setClickListener {
                viewModel.updateSupplier(
                    supplier.copy(
                        name = etName.text.toString(),
                        contactPerson = etContactPerson.text.toString(),
                        phone = etPhone.text.toString(),
                        email = etEmail.text.toString(),
                        address = etAddress.text.toString()
                    )
                )
            }
        }
    }

    private fun setAddMode() {
        binding.buttonSave.setClickListener {
            viewModel.addSupplier(
                SupplierUi(
                    name = binding.etName.text.toString(),
                    contactPerson = binding.etContactPerson.text.toString(),
                    phone = binding.etPhone.text.toString(),
                    email = binding.etEmail.text.toString(),
                    address = binding.etAddress.text.toString()
                )
            )
        }
    }
}