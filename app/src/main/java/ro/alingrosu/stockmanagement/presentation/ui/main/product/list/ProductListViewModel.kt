package ro.alingrosu.stockmanagement.presentation.ui.main.product.list

import ro.alingrosu.stockmanagement.domain.usecase.ProductUseCase
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : BaseViewModel() {

}