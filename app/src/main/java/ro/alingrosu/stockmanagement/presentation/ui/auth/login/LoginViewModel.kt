package ro.alingrosu.stockmanagement.presentation.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.domain.usecase.AuthUseCase
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    private val _uiState = MutableLiveData<UiState<Boolean>>()
    val uiState: LiveData<UiState<Boolean>> = _uiState

    fun login(user: String, pass: String) {
        compositeDisposable.add(
            authUseCase.authenticate(user, pass)
                .subscribeOn(Schedulers.io())
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