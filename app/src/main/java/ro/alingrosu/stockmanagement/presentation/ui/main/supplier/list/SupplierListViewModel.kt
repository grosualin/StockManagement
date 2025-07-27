package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.SupplierUseCase
import ro.alingrosu.stockmanagement.presentation.mapper.toUiModel
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.state.UiState.Loading.asEvent
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import ro.alingrosu.stockmanagement.presentation.util.Event
import javax.inject.Inject

class SupplierListViewModel @Inject constructor(
    private val supplierUseCase: SupplierUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<Event<UiState<List<SupplierUi>>>>()
    val uiState: LiveData<Event<UiState<List<SupplierUi>>>> = _uiState


    fun fetchSuppliers(query: String) {
        compositeDisposable.add(
            if (query.isBlank()) {
                supplierUseCase.getAllSuppliers()
            } else {
                supplierUseCase.searchSuppliers(query)
            }
                .subscribeOn(Schedulers.io())
                .map { it.toUiModel() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _uiState.value = UiState.Loading.asEvent()
                }
                .doOnError { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error").asEvent()
                }
                .subscribe({
                    _uiState.value = UiState.Success(it).asEvent()
                }, { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unexpected error occurred").asEvent()
                })
        )
    }
}