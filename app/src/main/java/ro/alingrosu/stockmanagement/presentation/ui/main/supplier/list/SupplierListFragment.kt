package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentDashboardBinding
import ro.alingrosu.stockmanagement.databinding.FragmentSupplierDetailBinding
import ro.alingrosu.stockmanagement.databinding.FragmentSupplierListBinding
import ro.alingrosu.stockmanagement.presentation.ui.main.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.ui.main.dashboard.DashboardViewModel
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class SupplierListFragment : BaseFragment(R.layout.fragment_supplier_list) {

    private lateinit var binding: FragmentSupplierListBinding

    private val viewModel: SupplierListViewModel by viewModels {
        Factory {
            getAppComponent().supplierListViewModel
        }
    }


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
    }

    override fun listenFoUiState() {
    }
}