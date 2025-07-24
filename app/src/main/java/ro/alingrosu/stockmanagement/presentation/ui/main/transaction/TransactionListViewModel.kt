package ro.alingrosu.stockmanagement.presentation.ui.main.transaction

import ro.alingrosu.stockmanagement.domain.usecase.TransactionUseCase
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : BaseViewModel() {

}