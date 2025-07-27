package ro.alingrosu.stockmanagement.presentation.ui.main.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.DashboardUseCase
import ro.alingrosu.stockmanagement.presentation.mapper.toUiModel
import ro.alingrosu.stockmanagement.presentation.model.DashboardUiModel
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.state.UiState.Loading.asEvent
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import ro.alingrosu.stockmanagement.presentation.util.Event
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val dashboardUseCase: DashboardUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<Event<UiState<DashboardUiModel>>>()
    val uiState: LiveData<Event<UiState<DashboardUiModel>>> = _uiState

    fun loadData() {
        compositeDisposable.add(
            dashboardUseCase.getDashboardData(5)
                .subscribeOn(Schedulers.io())
                .map {
                    DashboardUiModel(it.first.toUiModel(), it.second.toUiModel())
                }
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