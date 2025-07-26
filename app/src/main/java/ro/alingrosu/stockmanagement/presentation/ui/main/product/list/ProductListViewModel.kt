package ro.alingrosu.stockmanagement.presentation.ui.main.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.ProductUseCase
import ro.alingrosu.stockmanagement.presentation.mapper.toUiModel
import ro.alingrosu.stockmanagement.presentation.model.ProductUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<UiState<List<ProductUi>>>()
    val uiState: LiveData<UiState<List<ProductUi>>> = _uiState


    private fun Flowable<List<ProductUi>>.listen() = this.doOnSubscribe {
        _uiState.value = UiState.Loading
    }
        .doOnError { error ->
            _uiState.value = UiState.Error(error.message ?: "Unknown error")
        }
        .subscribe({
            _uiState.value = UiState.Success(it)
        }, { error ->
            _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred")
        })

    fun fetchProducts(query: String) {
        compositeDisposable.add(
            if (query.isBlank()) {
                productUseCase.getAllProducts()
            } else {
                productUseCase.searchProducts(query)
            }
                .subscribeOn(Schedulers.io())
                .map { it.toUiModel() }
                .observeOn(AndroidSchedulers.mainThread())
                .listen()
        )
    }
}