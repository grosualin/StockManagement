package ro.alingrosu.stockmanagement.presentation.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.alingrosu.stockmanagement.di.AppComponent
import ro.alingrosu.stockmanagement.presentation.StockApplication

class Factory<T : ViewModel>(private val create: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return create.invoke() as T
    }
}

fun Fragment.getAppComponent(): AppComponent = (requireContext().applicationContext as StockApplication).appComponent
