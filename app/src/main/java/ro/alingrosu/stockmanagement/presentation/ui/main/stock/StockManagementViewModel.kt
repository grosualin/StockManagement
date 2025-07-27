package ro.alingrosu.stockmanagement.presentation.ui.main.stock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.StockManagementUseCase
import ro.alingrosu.stockmanagement.presentation.mapper.toDomainModel
import ro.alingrosu.stockmanagement.presentation.mapper.toUiModel
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.model.TransactionUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.state.UiState.Loading.asEvent
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import ro.alingrosu.stockmanagement.presentation.util.Event
import javax.inject.Inject

class StockManagementViewModel @Inject constructor(
    private val stockManagementUseCase: StockManagementUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<Event<UiState<Boolean>>>()
    val uiState: LiveData<Event<UiState<Boolean>>> = _uiState

    private val _products = MutableLiveData<List<ProductUi>>()
    val products: LiveData<List<ProductUi>> = _products

    fun addStockTransaction(transactionUi: TransactionUi) {
        compositeDisposable.add(
            stockManagementUseCase.recordStockTransaction(transactionUi.toDomainModel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _uiState.value = UiState.Loading.asEvent()
                }
                .doOnError { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error").asEvent()
                }
                .subscribe({
                    _uiState.value = UiState.Success(true).asEvent()
                }, { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred").asEvent()
                })
        )
    }

    fun fetchProducts() {
        compositeDisposable.add(
            stockManagementUseCase.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .lastOrError()
                .map { suppliers -> suppliers.map { it.toUiModel() } }
                .subscribe({
                    _products.value = it
                }, { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred").asEvent()
                })
        )
    }

}