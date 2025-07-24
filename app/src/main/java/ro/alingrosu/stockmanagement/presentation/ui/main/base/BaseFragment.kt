package ro.alingrosu.stockmanagement.presentation.ui.main.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment


abstract class BaseFragment(@LayoutRes resId: Int) : Fragment(resId) {

    abstract fun initView()

    abstract fun listenFoUiState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        listenFoUiState()
    }
}