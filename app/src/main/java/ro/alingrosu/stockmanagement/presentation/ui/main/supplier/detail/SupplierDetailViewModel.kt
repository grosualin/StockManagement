package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.SupplierUseCase
import ro.alingrosu.stockmanagement.presentation.mapper.toDomainModel
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.state.UiState.Loading.asEvent
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import ro.alingrosu.stockmanagement.presentation.util.Event
import javax.inject.Inject

class SupplierDetailViewModel @Inject constructor(
    private val supplierUseCase: SupplierUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<Event<UiState<Boolean>>>()
    val uiState: LiveData<Event<UiState<Boolean>>> = _uiState

    fun updateSupplier(supplierUi: SupplierUi) {
        compositeDisposable.add(
            supplierUseCase.updateSupplier(supplierUi.toDomainModel())
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

    fun addSupplier(supplierUi: SupplierUi) {
        compositeDisposable.add(
            supplierUseCase.addSupplier(supplierUi.toDomainModel())
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

}