package ro.alingrosu.stockmanagement.presentation.ui.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentDashboardBinding
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.main.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding

    private val viewModel: DashboardViewModel by viewModels {
        Factory {
            getAppComponent().dashboardViewModel
        }
    }

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
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.loading.isVisible = true
                }

                is UiState.Success -> {
                    binding.loading.isVisible = false
                }

                is UiState.Error -> {
                    Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}