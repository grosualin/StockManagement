package ro.alingrosu.stockmanagement.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.ViewLoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewLoadingButtonBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            val buttonText = getString(R.styleable.LoadingButton_android_text)
            binding.buttonSubmit.text = buttonText
        }
    }

    var text: CharSequence
        get() = binding.buttonSubmit.text
        set(value) {
            binding.buttonSubmit.text = value
        }

    fun setClickListener(listener: OnClickListener) {
        binding.buttonSubmit.setOnClickListener(listener)
    }

    fun setLoading(loading: Boolean) {
        binding.buttonSubmit.isEnabled = !loading
        binding.loadingOverlay.isVisible = loading
    }
}