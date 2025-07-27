package ro.alingrosu.stockmanagement.presentation.state

import ro.alingrosu.stockmanagement.presentation.util.Event


sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()

    fun <T> UiState<T>.asEvent(): Event<UiState<T>> = Event(this)
}