package ro.alingrosu.stockmanagement.presentation.ui.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentDashboardBinding
import ro.alingrosu.stockmanagement.presentation.model.DashboardListItem
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels {
        Factory {
            getAppComponent().dashboardViewModel
        }
    }
    private val productAdapter = DashboardAdapter(::onItemClicked)
    private val transactionAdapter = DashboardAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentDashboardBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
        binding.rvLowStockItems.adapter = productAdapter
        binding.rvRecentTransactions.adapter = transactionAdapter
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { event ->
            event.contentIfNotHandled?.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.apply {
                            rvLowStockItems.isVisible = false
                            rvRecentTransactions.isVisible = false
                            tvLowStockItemsEmpty.isVisible = false
                            tvRecentTransactionsEmpty.isVisible = false
                            loadingRecentTransactions.isVisible = true
                            loadingLowStockItems.isVisible = true
                        }
                    }

                    is UiState.Success -> {
                        binding.apply {
                            rvLowStockItems.isVisible = uiState.data.lowStockItems.isNotEmpty()
                            rvRecentTransactions.isVisible = uiState.data.recentTransactions.isNotEmpty()
                            tvLowStockItemsEmpty.isVisible = uiState.data.lowStockItems.isEmpty()
                            tvRecentTransactionsEmpty.isVisible = uiState.data.recentTransactions.isEmpty()
                            loadingRecentTransactions.isVisible = false
                            loadingLowStockItems.isVisible = false
                        }
                        productAdapter.submitList(uiState.data.lowStockItems.map { DashboardListItem.ProductListItem(it) })
                        transactionAdapter.submitList(uiState.data.recentTransactions.map { DashboardListItem.TransactionListItem(it) })
                    }

                    is UiState.Error -> {
                        binding.apply {
                            rvLowStockItems.isVisible = false
                            rvRecentTransactions.isVisible = false
                            tvLowStockItemsEmpty.isVisible = true
                            tvRecentTransactionsEmpty.isVisible = true
                            loadingRecentTransactions.isVisible = false
                            loadingLowStockItems.isVisible = false
                        }
                        Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        viewModel.loadData()
    }

    private fun onItemClicked(item: DashboardListItem) {
        when (item) {
            is DashboardListItem.ProductListItem -> findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToProductDetailFragment(item.product)
            )

            else -> {

            }
        }
    }
}