package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentSupplierDetailBinding
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class SupplierDetailFragment : BaseFragment(R.layout.fragment_supplier_detail) {

    private lateinit var binding: FragmentSupplierDetailBinding

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
    }

    override fun listenFoUiState() {
    }
}