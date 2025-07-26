package ro.alingrosu.stockmanagement.presentation.ui.main.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.ProductUseCase
import ro.alingrosu.stockmanagement.domain.usecase.SupplierUseCase
import ro.alingrosu.stockmanagement.presentation.mapper.toDomainModel
import ro.alingrosu.stockmanagement.presentation.mapper.toUiModel
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val supplierUseCase: SupplierUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<UiState<Boolean>>()
    val uiState: LiveData<UiState<Boolean>> = _uiState

    private val _suppliers = MutableLiveData<List<SupplierUi>>()
    val suppliers: LiveData<List<SupplierUi>> = _suppliers

    fun updateProduct(productUi: ProductUi) {
        compositeDisposable.add(
            productUseCase.updateProduct(productUi.toDomainModel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _uiState.value = UiState.Loading
                }
                .doOnError { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
                .subscribe({
                    _uiState.value = UiState.Success(true)
                }, { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred")
                })
        )
    }

    fun addProduct(productUi: ProductUi) {
        compositeDisposable.add(
            productUseCase.addProduct(productUi.toDomainModel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _uiState.value = UiState.Loading
                }
                .doOnError { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
                .subscribe({
                    _uiState.value = UiState.Success(true)
                }, { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred")
                })
        )
    }

    fun fetchSuppliers() {
        compositeDisposable.add(
            supplierUseCase.getAllSuppliers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .lastOrError()
                .map { suppliers -> suppliers.map { it.toUiModel() } }
                .subscribe({
                    _suppliers.value = it
                }, { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred")
                })
        )
    }

}