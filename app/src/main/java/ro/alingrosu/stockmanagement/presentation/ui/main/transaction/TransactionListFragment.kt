package ro.alingrosu.stockmanagement.presentation.ui.main.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentTransactionListBinding
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent
import java.util.concurrent.TimeUnit

class TransactionListFragment : BaseFragment(R.layout.fragment_transaction_list) {

    private lateinit var binding: FragmentTransactionListBinding

    private val viewModel: TransactionListViewModel by viewModels {
        Factory {
            getAppComponent().transactionListViewModel
        }
    }
    private val transactionAdapter = TransactionAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentTransactionListBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
        binding.rvTransaction.adapter = transactionAdapter
        compositeDisposable.add(
            binding.svTransactions.queryTextChanges()
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.toString() }
                .subscribe({
                    viewModel.fetchTransactions(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { event ->
            event.contentIfNotHandled?.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.apply {
                            rvTransaction.isVisible = false
                            tvEmpty.isVisible = false
                            loading.isVisible = true
                        }
                    }

                    is UiState.Success -> {
                        binding.apply {
                            rvTransaction.isVisible = uiState.data.isNotEmpty()
                            tvEmpty.isVisible = uiState.data.isEmpty()
                            loading.isVisible = false
                        }
                        transactionAdapter.submitList(uiState.data)
                    }

                    is UiState.Error -> {
                        binding.apply {
                            rvTransaction.isVisible = false
                            tvEmpty.isVisible = true
                            loading.isVisible = false
                        }
                        Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        viewModel.fetchTransactions("")
    }
}