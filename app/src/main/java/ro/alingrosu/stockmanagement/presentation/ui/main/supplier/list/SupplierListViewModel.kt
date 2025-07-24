package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.list

import ro.alingrosu.stockmanagement.domain.usecase.SupplierUseCase
import ro.alingrosu.stockmanagement.presentation.ui.main.base.BaseViewModel
import javax.inject.Inject

class SupplierListViewModel @Inject constructor(
    private val supplierUseCase: SupplierUseCase
) : BaseViewModel() {

}