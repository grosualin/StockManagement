package ro.alingrosu.stockmanagement.presentation.ui.main.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.DashboardUseCase
import ro.alingrosu.stockmanagement.presentation.model.DashboardUiModel
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.BaseViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val dashboardUseCase: DashboardUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<UiState<DashboardUiModel>>()
    val uiState: LiveData<UiState<DashboardUiModel>> = _uiState

    fun loadData() {
        compositeDisposable.add(
            dashboardUseCase.getDashboardData()
                .subscribeOn(Schedulers.io())
                .map {
                    DashboardUiModel(it.first, it.second)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
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
        )
    }
}