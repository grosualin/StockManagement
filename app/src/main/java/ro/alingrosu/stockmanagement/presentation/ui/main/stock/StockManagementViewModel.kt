package ro.alingrosu.stockmanagement.presentation.ui.main.stock

import ro.alingrosu.stockmanagement.domain.usecase.StockManagementUseCase
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class StockManagementViewModel @Inject constructor(
    private val stockManagementUseCase: StockManagementUseCase
) : BaseViewModel() {

}